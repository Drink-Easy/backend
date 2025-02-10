package com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils;


import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils.AppleAuthClient;

import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.member.login.oauth2.apple.appleDTO.AppleRefreshTokenResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleProvider {

    @Value("${spring.servlet.social-login.provider.apple.client-id}")
    private String clientId;
    private final AppleAuthClient appleClient;

    public String getAppleRefreshToken(final String code, final String clientSecret) {
        try {

            AppleRefreshTokenResponseDTO appleRefreshTokenResponse = appleClient.getAppleToken(code, clientId, clientSecret,"authorization_code");

            if (appleRefreshTokenResponse.getError() != null || appleRefreshTokenResponse.getRefresh_token() == null) {
                throw new GeneralException(ErrorStatus.FAILED_TO_LOAD_REFRESH_TOKEN);
            }

            return appleRefreshTokenResponse.getRefresh_token();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException(ErrorStatus.FAILED_TO_LOAD_REFRESH_TOKEN);
        }
    }

    public void requestRevoke(final String refreshToken, final String clientSecret) {
        try{appleClient.revoke(clientSecret,refreshToken,clientId, "refresh_token");}
        catch(Exception e){
            e.printStackTrace();
        }
    }




}
