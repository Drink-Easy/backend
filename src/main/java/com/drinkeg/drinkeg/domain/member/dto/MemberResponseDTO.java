package com.drinkeg.drinkeg.domain.member.dto;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {

    private Long id;
    private String name;
    private String username;
    private Role role;

    private Boolean isNewbie;
    private Boolean isFirst;
    private Long monthPriceMax;

    @Builder.Default
    private List<String> wineSort = new ArrayList<>();
    @Builder.Default
    private List<String> wineArea = new ArrayList<>();

    private String region;

    private String imageUrl;

    public static MemberResponseDTO create(Member member){

        return  MemberResponseDTO.builder()
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
                .imageUrl(member.getImageUrl())
                .build();
    }
}
