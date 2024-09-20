package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.MemberDTO.response.MemberBasicInfoResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class MemberConverter {
    public Member toMember(String username, Map<String, Object> claims) {
        return Member.builder()
                .username(username)
                .email((String) claims.get("email"))  // email 값을 claims에서 추출
                .role("ROLE_USER")
                .isFirst(true)
                .build();
    }
  
    public static MemberBasicInfoResponseDTO toMemberBasicInfoResponseDTO(Member member) {
      return MemberBasicInfoResponseDTO.builder()
              .id(member.getId())
              .name(member.getName())
              .email(member.getEmail())
              .build();
    }
}
