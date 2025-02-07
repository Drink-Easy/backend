package com.drinkeg.drinkeg.domain.wine.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineUpdateRequest {
    private String name;
    private String nameEng;
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;
    private String sort;
    private String country;
    private String region;
    private String variety;
    @Min(value = 0, message = "비비노 평점은 0보다 커야 합니다.")
    @Max(value = 5, message = "비비노 평점은 5 이하여야 합니다.")
    private Float vivinoRating;

    @Builder
    public WineUpdateRequest(String name, String nameEng, Integer price, String sort, String country, String region, String variety, Float vivinoRating) {
        this.name = name;
        this.nameEng = nameEng;
        this.price = price;
        this.sort = sort;
        this.country = country;
        this.region = region;
        this.variety = variety;
        this.vivinoRating = vivinoRating;
    }
}
