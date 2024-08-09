package com.drinkeg.drinkeg.service.loginService;


import com.drinkeg.drinkeg.domain.RefreshToken;
import com.drinkeg.drinkeg.repository.RefreshRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshRepository refreshRepository;


    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true); //https를 사용할 경우
        cookie.setPath("/"); // 쿠키가 적용될 경로
        cookie.setHttpOnly(true);

        return cookie;
    }

    public void addRefreshToken(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setRefresh(refresh);
        refreshToken.setExpiration(date.toString());

        refreshRepository.save(refreshToken);
    }


}
