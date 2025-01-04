package com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO;


import com.drinkeg.drinkeg.domain.member.enums.Role;
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
