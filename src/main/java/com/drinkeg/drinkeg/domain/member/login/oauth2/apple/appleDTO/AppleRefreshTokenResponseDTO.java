package com.drinkeg.drinkeg.member.login.oauth2.apple.appleDTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppleRefreshTokenResponseDTO {

    private String access_token;
    private String expires_in;
    private String id_token;
    private String refresh_token;
    private String error;
}
