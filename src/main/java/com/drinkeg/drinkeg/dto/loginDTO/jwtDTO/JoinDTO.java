package com.drinkeg.drinkeg.dto.loginDTO.jwtDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDTO {

    private String username;
    private String password;
    private String rePassword;
}