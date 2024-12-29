package com.drinkeg.drinkeg.jwt;


import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.jwt.JWTUtil;
import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.member.enums.Role;
import com.drinkeg.drinkeg.redis.RedisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JWTUtil jwtUtil;
    private final RedisClient redisClient;

    public void createCookie(HttpServletResponse response, String key, String value) {

        ResponseCookie cookie = ResponseCookie.from(key, value)
                .httpOnly(true)
                .secure(true) // HTTPS만 허용
                .path("/")
                .sameSite("Strict") // SameSite 설정
                .maxAge(24 * 60 * 60) // 1일
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

    }

//    public void createRefreshCookie(HttpServletResponse response, String key, String value) {
//
//        ResponseCookie cookie = ResponseCookie.from(key, value)
//                .httpOnly(true)
//                .secure(true) // HTTPS만 허용
//                .path("/")
//                .sameSite("Strict") // SameSite 설정
//                .maxAge(24 * 60 * 60) // 1일
//                .build();
//        response.addHeader("Set-Cookie", cookie.toString());
//
//    }


    public void reissueRefreshToken(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refreshToken")) {

                    System.out.println("--------reissue controller-------");

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_NOT_FOUND);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            throw new GeneralException(ErrorStatus.REFRESH_TOKEN_EXPIRED);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //DB에 저장되어 있는지 확인
        String redisRefresh = redisClient.getValue(username);
        if (StringUtils.isEmpty(redisRefresh) || !refresh.equals(redisRefresh)) {

            //response body
            throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰 저장하고 기존의 Refresh 토큰 삭제 후에 새 refresh 토큰 저장
        redisClient.deleteValue(username);
        redisClient.setValue(username, newRefresh, 864000000L);

        //response
        createCookie(response, "accessToken", newAccess); // Access Token 쿠키 추가
        createCookie(response, "refreshToken", newRefresh); // refresh token 쿠키 추가
        response.setStatus(HttpStatus.OK.value());
    }

    public String decodeHeader(String token) {
        System.out.println("====decodeHeader====");
        return new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
    }
    // Identity Token 헤더에서 Public Key 사용에 필요한 헤더 추출
    public Map<String, String> parseHeaders(String identityToken) throws JsonProcessingException {
        System.out.println("====parseHeser====");
        String header = identityToken.split("\\.")[0];
        return new ObjectMapper().readValue(decodeHeader(header), Map.class);
    }

    public Claims getTokenClaims(final String token, final PublicKey publicKey) {
        try{
        return Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("지원되지 않는 jwt 타입");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("비어있는 jwt");
        } catch (JwtException e) {
            throw new JwtException("jwt 검증 or 분석 오류");
        }
    }

    public void deleteRefreshTokenAndAccessToken(HttpServletResponse response, String username){

        // 쿠키에 저장되어 있는 AccessToken과 RefreshToken 제거
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0); // 즉시 만료
        refreshTokenCookie.setPath("/"); // 애플리케이션 전역 경로

        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0); // 즉시 만료
        accessTokenCookie.setPath("/"); // 애플리케이션 전역 경로

        response.addCookie(refreshTokenCookie);
        response.addCookie(accessTokenCookie);

        redisClient.deleteValue(username);

    }

    public void jwtProvider(Member member, HttpServletResponse response) {

        String accessToken = jwtUtil.createJwt("access",member.getUsername(), member.getRole().toString(), 60000000000L); // 임의로 10000배로 해놓았음. 나중에 수정 필요.
        String refreshToken = jwtUtil.createJwt("refresh",member.getUsername(), member.getRole().toString(),864000000L);

        // 토큰을 쿠키에 저장하여 응답
        createCookie(response, "accessToken", accessToken); // Access Token 쿠키 추가
        createCookie(response, "refreshToken", refreshToken); // refresh token 쿠키 추가
        response.setStatus(HttpStatus.OK.value());

        // redis에 refresh 토큰 저장
        redisClient.setValue(member.getUsername(), refreshToken, 864000000L);
    }

    }





