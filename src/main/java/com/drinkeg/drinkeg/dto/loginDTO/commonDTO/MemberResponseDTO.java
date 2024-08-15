package com.drinkeg.drinkeg.dto.loginDTO.commonDTO;

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
    private String role;

    private Boolean isNewbie;
    private Boolean isFirst;
    private Long monthPriceMax;

    private List<String> wineSort = new ArrayList<>();
    private List<String> wineArea = new ArrayList<>();
    private List<String> wineVariety = new ArrayList<>();

    private String region;
}
