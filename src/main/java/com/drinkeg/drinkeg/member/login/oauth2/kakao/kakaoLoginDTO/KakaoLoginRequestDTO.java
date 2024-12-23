package com.drinkeg.drinkeg.member.login.oauth2.kakao.kakaoLoginDTO;


import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoLoginRequestDTO {

    @NotEmpty
    private String kakaoName;

    @NotEmpty
    private String kakaoEmail;
}


