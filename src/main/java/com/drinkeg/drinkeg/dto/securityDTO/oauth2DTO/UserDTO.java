package com.drinkeg.drinkeg.dto.securityDTO.oauth2DTO;


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
}
