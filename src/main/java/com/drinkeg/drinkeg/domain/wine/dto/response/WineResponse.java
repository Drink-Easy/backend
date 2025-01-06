package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class WineResponse {

    private Long wineId;

    private String name;

    private String imageUrl;

    private int price;
    private String sort;
    private String area;
    private String variety;
    private float vivinoRating;

    private float avgSugarContent;
    private float avgAcidity;
    private float avgTannin;
    private float avgBody;
    private float avgAlcohol;

    private String nose1;
    private String nose2;
    private String nose3;

    private float avgMemberRating;

    private boolean isLiked;


    @QueryProjection
    public WineResponse(
            Long wineId, String name, String imageUrl, int price, String sort, String area, String variety, float vivinoRating,
            float avgSugarContent, float avgAcidity, float avgTannin, float avgBody, float avgAlcohol,
            String nose1, String nose2, String nose3, float avgMemberRating, boolean isLiked) {

        this.wineId = wineId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.sort = sort;
        this.area = area;
        this.variety = variety;
        this.vivinoRating = vivinoRating;

        this.avgSugarContent = avgSugarContent;
        this.avgAcidity = avgAcidity;
        this.avgTannin = avgTannin;
        this.avgBody = avgBody;
        this.avgAlcohol = avgAlcohol;

        this.nose1 = nose1;
        this.nose2 = nose2;
        this.nose3 = nose3;
        this.avgMemberRating = avgMemberRating;

        this.isLiked = isLiked;
    }

}
