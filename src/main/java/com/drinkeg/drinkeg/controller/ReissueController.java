package com.drinkeg.drinkeg.controller;


import com.drinkeg.drinkeg.jwt.JWTUtil;
import com.drinkeg.drinkeg.repository.RefreshRepository;
import com.drinkeg.drinkeg.service.loginService.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor
public class ReissueController {


    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refreshToken")) {

                System.out.println("--------reissue controller-------");
                refresh = cookie.getValue();
                System.out.println("ReissueToken: " +refresh);
            }
        }
        if (refresh == null) {
            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }


        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        System.out.println("newAccessToken:"+newAccess);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
        System.out.println("newRefreshToken:"+newRefresh);

        //Refresh 토큰 저장하고 기존의 Refresh 토큰 삭제 후에 새 refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        tokenService.addRefreshToken(username, newRefresh, 86400000L);

        //response
        response.addCookie(tokenService.createCookie("accessToken", newAccess));
        response.addCookie(tokenService.createCookie("refreshToken", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
