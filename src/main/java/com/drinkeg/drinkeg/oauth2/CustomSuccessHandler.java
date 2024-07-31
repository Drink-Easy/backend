package com.drinkeg.drinkeg.oauth2;


import com.drinkeg.drinkeg.domain.RefreshToken;
import com.drinkeg.drinkeg.dto.CustomOAuth2User;
import com.drinkeg.drinkeg.jwt.JWTUtil;
import com.drinkeg.drinkeg.repository.RefreshRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    // CustomSuccessHandler(JWTUtil jwtUtil) {

        //this.jwtUtil = jwtUtil;
    //}

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 생성
        String accessToken = jwtUtil.createJwt("access",username, role, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh",username,role,86400000L);

        System.out.println("---------------customSuccessHandler------------------");

        System.out.println("accessToken  ===  " + accessToken);
        System.out.println("refreshToken == " + refreshToken);



        // 토큰을 쿠키에 저장하여 응답 (access 의 경우 추후 프론트와 협의하여 헤더에 넣어서 반환할 예정)
        response.addCookie(createCookie("accessToken", accessToken));
        response.addCookie(createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());

        // refresh 토큰 저장
        addRefreshToken(username, refreshToken, 86400000L);

        response.sendRedirect("http://localhost:8080/main");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true); //https를 사용할 경우
        cookie.setPath("/"); // 쿠키가 적용될 경로
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshToken(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpiration(date.toString());

        refreshRepository.save(refreshToken);
    }
}
