package com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils;

import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleDTO.ApplePublicKeyResponseDTO;
import com.drinkeg.drinkeg.member.login.oauth2.apple.appleDTO.AppleRefreshTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


// 공개키를 요청함
// url은 https://appleid.apple.com/auth/keys

@FeignClient(name= "appleAuthClient", url = "https://appleid.apple.com/auth")
public interface AppleAuthClient {
    @GetMapping("/keys")
    ApplePublicKeyResponseDTO getAppleAuthPublicKey();

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AppleRefreshTokenResponseDTO getAppleToken(@RequestParam(value = "code") String code,
                                               @RequestParam(value = "client_id") String client_id,
                                               @RequestParam(value = "client_secret") String client_secret,
                                               @RequestParam(value = "grant_type") String grant_type);


    @PostMapping(value = "/auth/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void revoke(@RequestParam("token") String token,
                @RequestParam("client_id") String clientId,
                @RequestParam("client_secret") String clientSecret,
                @RequestParam("token_type_hint") String tokenTypeHint);

}
