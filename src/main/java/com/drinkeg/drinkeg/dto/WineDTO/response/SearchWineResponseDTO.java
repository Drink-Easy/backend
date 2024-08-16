package com.drinkeg.drinkeg.dto.WineDTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchWineResponseDTO {

    private Long wineId;
    private String name;
    private String imageUrl;

    private String sort;
    private String area;

    private float rating;

    private int price;

}
