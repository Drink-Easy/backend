package com.drinkeg.drinkeg.jwt;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.LoginResponse;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.redis.RedisClient;
import com.drinkeg.drinkeg.service.loginService.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.drinkeg.drinkeg.jwt.JWTException.*;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final RedisClient redisClient;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 클라이언트 요청에서 username, password 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestBody;

        try {
            requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse authentication request body", e);
        }

        if (requestBody.get("username") == null) {
            jwtExceptionHandler(response, ErrorStatus.USERNAME_NOT_FOUND);
            return null;
        }
        if (requestBody.get("password") == null) {
            jwtExceptionHandler(response, ErrorStatus.PASSWORD_NOT_FUND);
            return null;
        }
        String username = requestBody.get("username");
        String password = requestBody.get("password");

        // 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공시 실행하는 메서드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();

        String username = principalDetail.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String accessToken = jwtUtil.createJwt("access",username, role, 60000000000L); // 임의로 10000배로 해놓았음. 나중에 수정 필요.
        String refreshToken = jwtUtil.createJwt("refresh",username,role,864000000L);

        System.out.println("---------------LoginFilter------------------");


        // 토큰을 쿠키에 저장하여 응답 (access 의 경우 추후 프론트와 협의하여 헤더에 넣어서 반환할 예정)
        response.addCookie(tokenService.createCookie("accessToken", accessToken));
        response.addCookie(tokenService.createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());

        // redis에 refresh 토큰 저장
        redisClient.setValue(username, refreshToken, 864000000L);

        // 첫 로그인 여부 가져오기
        Boolean isFirst = principalDetail.getIsFirst();

        LoginResponse loginResponse = LoginResponse.builder()
                .username(username)
                .role(role)
                .isFirst(isFirst)
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), loginResponse);

    }

    // 로그인 실패시 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 실패 시 상태 코드 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", "MEMBER4001");
        errorResponse.put("message", "로그인 과정에서 오류가 발생했습니다.");

        try {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to write authentication response body", e);
        }
    }
}