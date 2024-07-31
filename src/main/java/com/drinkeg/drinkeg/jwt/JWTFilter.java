package com.drinkeg.drinkeg.jwt;

import com.drinkeg.drinkeg.dto.CustomOAuth2User;
import com.drinkeg.drinkeg.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        System.out.println("-------------------------JWT FILTER------------------------");
        String requestUri = request.getRequestURI();


        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }

        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String accessToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            System.out.println("cookie = " + cookie.getName()+"= "+cookie.getValue());
            if (cookie.getName().equals("accessToken")) {

                accessToken = cookie.getValue();
                System.out.println("accessToken: "+ accessToken);
            }
        }



        //Authorization 헤더 검증
        if (accessToken == null) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = accessToken;

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰이 access 토큰인지 확인
        String category = jwtUtil.getCategory(accessToken);

        if(!category.equals("access")){

            System.out.println("is not access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        System.out.println("---------------------------JWT FIlter 2________________________");
        System.out.println("jwt username = " + username);


        //userDTO를 생성하여 값 set
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRole(role);

        System.out.println(userDTO);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        System.out.println("authToken "+ authToken);
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
