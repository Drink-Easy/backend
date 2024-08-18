package com.drinkeg.drinkeg.fegin;

import com.drinkeg.drinkeg.dto.AppleLoginDTO.ApplePublicKeyResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;


// 공개키를 요청함
// url은 https://appleid.apple.com/auth/keys

@FeignClient(name= "appleAuthClient", url = "https://appleid.apple.com/auth/keys")
public interface AppleAuthClient {
    @GetMapping
    ApplePublicKeyResponseDTO getAppleAuthPublicKey();
}
