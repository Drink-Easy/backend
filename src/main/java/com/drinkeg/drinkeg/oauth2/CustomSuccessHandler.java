package com.drinkeg.drinkeg.oauth2;


import com.drinkeg.drinkeg.dto.CustomOAuth2User;
import com.drinkeg.drinkeg.jwt.JWTUtil;
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
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

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

        String accessToken = jwtUtil.createJwt("access",username, role, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh",username,role,86400000L);

        System.out.println("---------------customSuccessHandler------------------");

        System.out.println("accessToken  ===  " + accessToken);
        System.out.println("refreshToken == " + refreshToken);

        response.addCookie(createCookie("accessToken", accessToken));
        response.addCookie(createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());


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
}
