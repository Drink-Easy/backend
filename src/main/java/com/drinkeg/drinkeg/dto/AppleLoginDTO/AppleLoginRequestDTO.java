package com.drinkeg.drinkeg.dto.AppleLoginDTO;


import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleLoginRequestDTO {

    @NotEmpty
    private String identityToken;  // 프론트한테 유저 정보가 들어있는 identityToken
}
