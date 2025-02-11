package com.drinkeg.drinkeg.global.security.jwt;

import com.drinkeg.drinkeg.domain.member.login.oauth2.dto.LoginResponseDTO;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.infra.redis.RedisClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RedisClient redisClient;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Servlet -> HttpServlet으로 캐스트
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 경로("/logout")와 메서드(POST) 검증
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/logout$")) {

            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {

            JWTException.jwtExceptionHandler(response, ErrorStatus.METHOD_NOT_ALLOWED);
            return;
        }

        // 쿠키에서 Refresh 토큰 가져옴
        String refresh = null;
        String access = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {  // ✅ `cookies`가 `null`이 아닐 때만 순회
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refresh = cookie.getValue();
                } else if (cookie.getName().equals("accessToken")) {
                    access = cookie.getValue();
                }
            }
        }
        if (access == null) {
            JWTException.jwtExceptionHandler(response, ErrorStatus.ACCESS_TOKEN_NOT_FOUND);
            return;
        }

        // 토큰 존재 여부 확인
        if (refresh == null) {
            // response status code
            JWTException.jwtExceptionHandler(response, ErrorStatus.REFRESH_TOKEN_NOT_FOUND);
            return;
        }


        try {
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e) {
            JWTException.jwtExceptionHandler(response, ErrorStatus.ACCESS_TOKEN_EXPIRED);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            JWTException.jwtExceptionHandler(response, ErrorStatus.INVALID_REFRESH_TOKEN);
            return;
        }

        String accessCategory = jwtUtil.getCategory(access);
        if (!accessCategory.equals("access")) {
            JWTException.jwtExceptionHandler(response, ErrorStatus.INVALID_ACCESS_TOKEN);
            return;
        }


        String username = jwtUtil.getUsername(refresh);

        // DB에 저장되어 있는지 확인
        String redisRefresh = redisClient.getValue(username);
        if (StringUtils.isEmpty(redisRefresh) || !refresh.equals(redisRefresh)) {
            // response body
            JWTException.jwtExceptionHandler(response, ErrorStatus.INVALID_REFRESH_TOKEN);
            return;
        }

        // 로그아웃 진행
        // Refresh 토큰 DB에서 제거
        redisClient.deleteValue(username);

        // 쿠키에 저장되어 있는 Refresh 토큰, Access 토큰 null값 처리
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0)
                .secure(true)
                .path("/")
                .httpOnly(true)
                .sameSite("Strict")
                .build();

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", null)
                .maxAge(0)
                .secure(true)
                .path("/")
                .httpOnly(true)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.setStatus(HttpServletResponse.SC_OK);

        ApiResponse<String> apiResponse = ApiResponse.onSuccess("로그아웃 성공");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}