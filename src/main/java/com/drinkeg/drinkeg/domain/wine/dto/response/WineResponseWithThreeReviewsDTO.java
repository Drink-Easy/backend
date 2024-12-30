package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WineResponseWithThreeReviewsDTO {
    WineResponseDTO wineResponseDTO;
    List<WineReviewResponseDTO> recentReviews;

    @QueryProjection
    public WineResponseWithThreeReviewsDTO(WineResponseDTO wineResponseDTO, List<WineReviewResponseDTO> recentReviews) {
        this.wineResponseDTO = wineResponseDTO;
        this.recentReviews = recentReviews;
    }
}
