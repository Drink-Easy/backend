package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import lombok.Builder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HomeWineResponse {

    private Long wineId;
    private String imageUrl;

    private String wineName;
    private String wineNameEng;
    private String sort;
    private int price;

    private float vivinoRating;

    @Builder
    public HomeWineResponse(Long wineId, String imageUrl, String wineName, String wineNameEng, String sort, int price, float vivinoRating){
        this.wineId = wineId;
        this.imageUrl = imageUrl;
        this.wineName = wineName;
        this.wineNameEng = wineNameEng;
        this.sort = sort;
        this.price = price;
        this.vivinoRating = vivinoRating;
    }

    public static HomeWineResponse of(Wine wine){
        return HomeWineResponse.builder()
                .wineId(wine.getId())
                .imageUrl(wine.getImageUrl())
                .wineName(wine.getName())
                .wineNameEng(wine.getNameEng())
                .sort(wine.getSort())
                .price(wine.getPrice())
                .vivinoRating(wine.getVivinoRating())
                .build();
    }
}
