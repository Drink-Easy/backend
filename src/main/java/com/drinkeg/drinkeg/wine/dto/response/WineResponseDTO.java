package com.drinkeg.drinkeg.wine.dto.response;

import com.drinkeg.drinkeg.wineNote.domain.WineNoteNose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
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

}
