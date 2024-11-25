package com.drinkeg.drinkeg.wine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WineResponseWithThreeReviewsDTO {
    WineResponseDTO wineResponseDTO;
    List<WineReviewResponseDTO> recentReviews;
}
