package com.drinkeg.drinkeg.dto.loginDTO.jwtDTO;

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
public class MemberRequestDTO {

    private Boolean isNewbie;
    private Long monthPrice;

    private List<String> wineSort = new ArrayList<>();
    private List<String> wineNation = new ArrayList<>();
    private List<String> wineVariety = new ArrayList<>();

    private String region;
}
