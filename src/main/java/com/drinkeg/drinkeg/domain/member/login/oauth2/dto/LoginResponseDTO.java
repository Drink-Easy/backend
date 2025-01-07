package com.drinkeg.drinkeg.domain.member.login.oauth2.dto;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNotePreviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private Long id;
    private String username;
    private Role role;
    private Boolean isFirst;
    // private String refreshToken

    public static LoginResponseDTO create(Long id, String username, Role role,Boolean isFirst){
        return LoginResponseDTO.builder()
                .id(id)
                .username(username)
                .role(role)
                .isFirst(isFirst)
                .build();
    }

    }
