package com.drinkeg.drinkeg.dto.HomeDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendWineDTO {

    private Long wineId;

    private String wineName;

    private String imageUrl;
}
