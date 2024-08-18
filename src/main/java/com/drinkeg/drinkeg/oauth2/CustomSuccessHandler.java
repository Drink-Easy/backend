package com.drinkeg.drinkeg.oauth2;


import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.PrincipalDetail;
import com.drinkeg.drinkeg.jwt.JWTUtil;
//import com.drinkeg.drinkeg.redis.RedisClient;
import com.drinkeg.drinkeg.service.loginService.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    //private final RedisClient redisClient;

    // CustomSuccessHandler(JWTUtil jwtUtil) {

        //this.jwtUtil = jwtUtil;
    //}

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();

        String username = principalDetail.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 생성
        String accessToken = jwtUtil.createJwt("access",username, role, 60000000000L); // 임의로 10000배로 해놓았음. 나중에 수정 필요.
        String refreshToken = jwtUtil.createJwt("refresh",username,role,864000000L);

        System.out.println("---------------customSuccessHandler------------------");



        // 토큰을 쿠키에 저장하여 응답 (access 의 경우 추후 프론트와 협의하여 헤더에 넣어서 반환할 예정)
        response.addCookie(tokenService.createCookie("accessToken", accessToken));
        response.addCookie(tokenService.createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());

        // redis에 refresh 토큰 저장
        //redisClient.setValue(username, refreshToken, 864000000L);

        // response.sendRedirect("https://drinkeg.com/maindy");

//        LoginResponse loginResponse = LoginResponse.builder()
//                .username(username)
//                .role(role)
//                .accessToken(token)
//                .build();
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.writeValue(response.getWriter(), loginResponse);

//        System.out.println("token  ===  " +token);
    }



}
