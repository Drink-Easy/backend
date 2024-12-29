package com.drinkeg.drinkeg.dto.loginDTO.commonDTO;


import com.drinkeg.drinkeg.member.enums.Role;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Role role;
    private String name;
    private String username;
    private String password;

    private Boolean isFirst;
}
