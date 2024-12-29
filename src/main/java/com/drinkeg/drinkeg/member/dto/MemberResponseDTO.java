package com.drinkeg.drinkeg.member.dto;

import com.drinkeg.drinkeg.member.enums.Role;
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
}
