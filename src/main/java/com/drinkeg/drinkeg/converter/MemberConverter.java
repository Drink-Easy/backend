package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.MemberDTO.response.MemberBasicInfoResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {
    public static MemberBasicInfoResponseDTO toMemberBasicInfoResponseDTO(Member member) {
        return MemberBasicInfoResponseDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}
