package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.MemberResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.UserDTO;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.OAuth2Response;
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

    public static Member toOauth2Member(String username, OAuth2Response oAuth2Response) {
        return Member.builder()
                .username(username)
                .email(oAuth2Response.getEmail())
                .name(oAuth2Response.getName())
                .role("ROLE_USER")
                .isFirst(true)
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
