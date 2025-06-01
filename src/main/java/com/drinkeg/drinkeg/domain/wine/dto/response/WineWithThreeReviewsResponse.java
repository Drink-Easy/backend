package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineWithThreeReviewsResponse {
    private WineInfoResponse wineInfoResponse;
    private List<WineReviewResponse> recentReviews;

    @Builder
    public WineWithThreeReviewsResponse(WineInfoResponse wineInfoResponse, List<WineReviewResponse> recentReviews) {
        this.wineInfoResponse = wineInfoResponse;
        this.recentReviews = recentReviews;
    }

    public static WineWithThreeReviewsResponse of(WineVintage wineVintage, List<TastingNote> recentTastingNotes, boolean isLiked) {
        WineInfoResponse wineInfoResponse = WineInfoResponse.of(wineVintage, isLiked);

        List<WineReviewResponse> wineReviewResponses = recentTastingNotes.stream()
                .map(WineReviewResponse::of)
                .toList();

        return WineWithThreeReviewsResponse.builder()
                .wineInfoResponse(wineInfoResponse)
                .recentReviews(wineReviewResponses)
                .build();
    }
}
