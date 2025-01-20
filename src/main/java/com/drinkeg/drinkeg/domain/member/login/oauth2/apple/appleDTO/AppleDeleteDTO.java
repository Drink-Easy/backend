package com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleDTO;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleDeleteDTO {

    private String authorizationCode;
}
