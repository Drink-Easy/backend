package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminWineDetailResponse {


    private Long wineId;
    private String name;
    private String nameEng;
    private String imageUrl;
    private String sort;
    private String country;
    private String region;
    private String variety;
    private float vivinoRating;
    private int price;
    private LocalDateTime createdAt;


    @Builder
    public AdminWineDetailResponse(Long wineId, String name, String nameEng, String imageUrl,
                             String sort, String country, String region,  String variety,
                             float vivinoRating, int price) {
        this.wineId = wineId;
        this.name = name;
        this.nameEng = nameEng;
        this.imageUrl = imageUrl;
        this.sort = sort;
        this.country = country;
        this.region = region;
        this.variety = variety;
        this.vivinoRating = vivinoRating;
        this.price = price;
    }

    public static AdminWineDetailResponse of(Wine wine) {
        return AdminWineDetailResponse.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .nameEng(wine.getNameEng())
                .imageUrl(wine.getImageUrl())
                .sort(wine.getSort())
                .country(wine.getCountry())
                .region(wine.getRegion())
                .variety(wine.getVariety())
                .vivinoRating(wine.getVivinoRating())
                .price(wine.getPrice())
                .build();
    }
}
