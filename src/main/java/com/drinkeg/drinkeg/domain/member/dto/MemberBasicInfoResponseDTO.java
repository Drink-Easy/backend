package com.drinkeg.drinkeg.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberBasicInfoResponseDTO {
    private Long id;
    private String name;
    private String email;
}
