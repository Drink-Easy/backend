package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WineWithThreeReviewsResponse {
    WineResponse wineResponse;
    List<WineReviewResponse> recentReviews;

    @QueryProjection
    public WineWithThreeReviewsResponse(WineResponse wineResponse, List<WineReviewResponse> recentReviews) {
        this.wineResponse = wineResponse;
        this.recentReviews = recentReviews;
    }
}
