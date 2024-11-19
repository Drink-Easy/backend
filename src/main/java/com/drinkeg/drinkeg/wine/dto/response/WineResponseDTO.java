package com.drinkeg.drinkeg.wine.dto.response;

import com.drinkeg.drinkeg.wineNote.domain.WineNoteNose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineResponseDTO {

    private Long wineId;

    private String name;

    private String imageUrl;

    private int price;
    private String sort;
    private String area;

    private float sugarContent;
    private float acidity;
    private float tannin;
    private float body;
    private float alcohol;

    private WineNoteNose wineNoteNose;

    private float satisfaction;

}
