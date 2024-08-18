package com.drinkeg.drinkeg.jwt;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.exception.GeneralException;
//import com.drinkeg.drinkeg.redis.RedisClient;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    //private final RedisClient redisClient;

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

            filterChain.doFilter(request, response);
            return;
        }

        // 쿠키에서 Refresh 토큰 가져옴
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refreshToken")) {

                refresh = cookie.getValue();
            }
        }

        // 토큰 존재 여부 확인
        if (refresh == null) {

            // response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            // response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {

            // response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String username = jwtUtil.getUsername(refresh);

        //DB에 저장되어 있는지 확인
        //String redisRefresh = redisClient.getValue(username);
        //if (StringUtils.isEmpty(redisRefresh) || !refresh.equals(redisRefresh)) {

            //response body
            //throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN);
        //}

        // 로그아웃 진행
        // Refresh 토큰 DB에서 제거
        //redisClient.deleteValue(username);

        // 쿠키에 저장되어 있는 Refresh 토큰 null값 처리
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}