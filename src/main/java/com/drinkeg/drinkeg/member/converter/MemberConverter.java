package com.drinkeg.drinkeg.member.converter;

import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.member.dto.MemberResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.UserDTO;
import com.drinkeg.drinkeg.member.dto.MemberBasicInfoResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class MemberConverter {

    public Member toAppleMember(String username, Map<String, Object> claims) {
        return Member.builder()
                .username(username)
                .email((String) claims.get("email"))  // email 값을 claims에서 추출
                .role("ROLE_USER")
                .isFirst(true)
                .build();
    }

    public Member toKakaoMember(String kakaoname, String email ) {
        return Member.builder()
                .username(kakaoname)
                .email(email)
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
    public Member toMember(String username, String password, boolean isBoolean){
        return Member.builder()
                .username(username)
                .password(password)
                .role("ROLE_USER")
                .isFirst(isBoolean)
                .build();
    }


    public static MemberResponseDTO toMemberResponseDTO(Member member) {
        return MemberResponseDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .username(member.getUsername())
                .role(member.getRole())
                .isNewbie(member.getIsNewbie())
                .isFirst(member.getIsFirst())
                .monthPriceMax(member.getMonthPriceMax())
                .wineSort(member.getWineSort())
                .wineArea(member.getWineArea())
                .wineVariety(member.getWineVariety())
                .region(member.getRegion())
                .build();
    }

    public static UserDTO toUserDTO (Member member) {
        return UserDTO.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .role(member.getRole())
                .isFirst(member.getIsFirst())
                .build();
    }


}
