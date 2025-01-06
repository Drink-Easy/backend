package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WineWithThreeReviewsResponse {
    WineInfoResponse wineInfoResponse;
    List<WineReviewResponse> recentReviews;

    @Builder
    public WineWithThreeReviewsResponse(WineInfoResponse wineInfoResponse, List<WineReviewResponse> recentReviews) {
        this.wineInfoResponse = wineInfoResponse;
        this.recentReviews = recentReviews;
    }

    public static WineWithThreeReviewsResponse of(Wine wine, List<TastingNote> recentTastingNotes, boolean isLiked) {
        WineInfoResponse wineInfoResponse = WineInfoResponse.of(wine, isLiked);

        List<WineReviewResponse> wineReviewResponses = recentTastingNotes.stream()
                .map(WineReviewResponse::of)
                .toList();

        return WineWithThreeReviewsResponse.builder()
                .wineInfoResponse(wineInfoResponse)
                .recentReviews(wineReviewResponses)
                .build();
    }
}
