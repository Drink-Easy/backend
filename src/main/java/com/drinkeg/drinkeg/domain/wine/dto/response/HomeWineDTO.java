package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HomeWineDTO {

    private Long wineId;
    private String imageUrl;

    private String wineName;
    private String sort;
    private int price;

    private float vivinoRating;

    @QueryProjection
    public HomeWineDTO(Long wineId, String imageUrl, String wineName, String sort, int price, float vivinoRating){
        this.wineId = wineId;
        this.imageUrl = imageUrl;

        this.wineName = wineName;
        this.sort = sort;
        this.price = price;

        this.vivinoRating = vivinoRating;
    }
}
