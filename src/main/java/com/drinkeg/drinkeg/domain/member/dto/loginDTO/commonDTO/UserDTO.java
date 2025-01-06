package com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO;


import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private Role role;
    private String name;
    private String username;
    private String password;

    private Boolean isFirst;

    public static UserDTO create (Member member) {
        return UserDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .role(member.getRole())
                .isFirst(member.getIsFirst())
                .build();
    }
}
