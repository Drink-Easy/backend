package com.drinkeg.drinkeg.dto.loginDTO.commonDTO;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String role;
    private String name;
    private String username;
    private String password;

    private Boolean isFirst;
}
