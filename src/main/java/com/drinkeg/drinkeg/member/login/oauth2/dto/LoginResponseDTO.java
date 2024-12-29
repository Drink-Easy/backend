package com.drinkeg.drinkeg.member.login.oauth2.dto;

import com.drinkeg.drinkeg.member.enums.Role;
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
    private Role role;
    private Boolean isFirst;
    // private String refreshToken
}
