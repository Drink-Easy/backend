package com.drinkeg.drinkeg.wine.dto.response;

import com.drinkeg.drinkeg.wineNote.domain.WineNoteNose;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class WineResponseDTO {

    private Long wineId;

    private String name;

    private String imageUrl;

    private int price;
    private String sort;
    private String area;
    private float satisfaction;

    private float avgSugarContent;
    private float avgAcidity;
    private float avgTannin;
    private float avgBody;
    private float avgAlcohol;

    private WineNoteNose wineNoteNose;

    private float avgSatisfaction;

    private boolean isLiked;


    @QueryProjection
    public WineResponseDTO(
            Long wineId, String name, String imageUrl, int price, String sort, String area, float satisfaction,
            float avgSugarContent, float avgAcidity, float avgTannin, float avgBody, float avgAlcohol,
            WineNoteNose wineNoteNose, float avgSatisfaction, boolean isLiked) {

        this.wineId = wineId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.sort = sort;
        this.area = area;
        this.satisfaction = satisfaction;

        this.avgSugarContent = avgSugarContent;
        this.avgAcidity = avgAcidity;
        this.avgTannin = avgTannin;
        this.avgBody = avgBody;
        this.avgAlcohol = avgAlcohol;

        this.wineNoteNose = wineNoteNose;
        this.avgSatisfaction = avgSatisfaction;

        this.isLiked = isLiked;
    }

}
