package com.drinkeg.drinkeg.dto.HomeDTO;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class RecommendWineDTO {

    private Long wineId;
    private String wineName;
    private String imageUrl;

    @QueryProjection
    public RecommendWineDTO(Long wineId, String wineName, String imageUrl){
        this.wineId = wineId;
        this.wineName = wineName;
        this.imageUrl = imageUrl;
    }
}
