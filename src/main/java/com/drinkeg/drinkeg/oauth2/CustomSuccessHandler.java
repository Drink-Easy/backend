package com.drinkeg.drinkeg.oauth2;


import com.drinkeg.drinkeg.dto.CustomOAuth2User;
import com.drinkeg.drinkeg.dto.LoginResponse;
import com.drinkeg.drinkeg.dto.PrincipalDetail;
import com.drinkeg.drinkeg.jwt.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
        PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();

        String username = principalDetail.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*60L);

        // response.addCookie(createCookie("Authorization", token));

        LoginResponse loginResponse = LoginResponse.builder()
                .username(username)
                .role(role)
                .accessToken(token)
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), loginResponse);

        System.out.println("token  ===  " +token);
        // response.sendRedirect("http://localhost:8080/main");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
