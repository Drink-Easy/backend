package com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String username;
    private String role;
    private Boolean isFirst;
    // private String refreshToken
}
