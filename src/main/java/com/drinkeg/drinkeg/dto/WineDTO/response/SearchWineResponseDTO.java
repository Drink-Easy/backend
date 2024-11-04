package com.drinkeg.drinkeg.dto.WineDTO.response;

import lombok.*;

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

    private boolean isLiked;

}
