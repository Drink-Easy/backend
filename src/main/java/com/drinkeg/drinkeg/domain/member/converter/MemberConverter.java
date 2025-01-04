package com.drinkeg.drinkeg.domain.member.converter;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.MemberResponseDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.UserDTO;
import com.drinkeg.drinkeg.domain.member.dto.MemberBasicInfoResponseDTO;

import com.drinkeg.drinkeg.domain.member.enums.Provider;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class MemberConverter {



  
    public static MemberBasicInfoResponseDTO toMemberBasicInfoResponseDTO(Member member) {
      return MemberBasicInfoResponseDTO.builder()
              .id(member.getId())
              .name(member.getName())
              .email(member.getEmail())
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
