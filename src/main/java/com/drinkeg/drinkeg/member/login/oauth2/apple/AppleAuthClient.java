package com.drinkeg.drinkeg.member.login.oauth2.apple;

import com.drinkeg.drinkeg.member.login.oauth2.apple.appleDTO.ApplePublicKeyResponseDTO;
import com.drinkeg.drinkeg.member.login.oauth2.apple.appleDTO.AppleRefreshTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;


// 공개키를 요청함
// url은 https://appleid.apple.com/auth/keys

@FeignClient(name= "appleAuthClient", url = "https://appleid.apple.com/auth")
public interface AppleAuthClient {
    @GetMapping("/keys")
    ApplePublicKeyResponseDTO getAppleAuthPublicKey();

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AppleRefreshTokenResponseDTO getAppleToken(@RequestPart(value = "code") String code,
                                                              @RequestPart(value = "client_id") String client_id,
                                                              @RequestPart(value = "client_secret") String client_secret,
                                                              @RequestPart(value = "grant_type") String grant_type);


    @PostMapping(value = "/revoke")
    void revoke(@RequestPart(value = "token") String token,
                @RequestPart(value = "client_id") String client_id,
                @RequestPart(value = "client_secret") String client_secret,
                @RequestPart(value = "token_type_hint") String token_type_hint);
}
