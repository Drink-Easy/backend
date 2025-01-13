package com.drinkeg.drinkeg.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class MemberRequest {

    @NotBlank
    private String name;

    @NotNull
    private Boolean isNewbie;

    @NotNull
    private Long monthPrice;

    @Builder.Default
    private List<String> wineSort = new ArrayList<>();

    @Builder.Default
    private List<String> wineArea = new ArrayList<>();

    @Builder.Default
    private List<String> wineVariety = new ArrayList<>();

    @NotBlank
    private String region;
}
