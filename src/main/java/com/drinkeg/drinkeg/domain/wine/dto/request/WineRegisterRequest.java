package com.drinkeg.drinkeg.domain.wine.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineRegisterRequest {

    @NotBlank(message = "와인 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "와인 영문 이름은 필수입니다.")
    private String nameEng;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private int price;

    @NotBlank(message = "종류는 필수입니다.")
    private String sort;

    @NotBlank(message = "국가는 필수입니다.")
    private String country;

    @NotBlank(message = "생산지는 필수입니다.")
    private String region;

    @NotBlank(message = "품종은 필수입니다.")
    private String variety;

    @Min(value = 0, message = "비비노 평점은 0보다 커야 합니다.")
    @Max(value = 5, message = "비비노 평점은 5 이하여야 합니다.")
    private float vivinoRating;

    @Builder
    public WineRegisterRequest(String name, String nameEng, int price, String sort, String country, String region, String variety, float vivinoRating) {
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