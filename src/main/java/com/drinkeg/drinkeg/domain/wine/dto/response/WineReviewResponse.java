package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WineReviewResponse {
    private String name;

    private String review;

    private float rating;

    private LocalDateTime createdAt;

    @Builder
    public WineReviewResponse(String name, String review, float rating, LocalDateTime createdAt) {
        this.name = name;
        this.review = review;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public static WineReviewResponse of(TastingNote tastingNote) {
        return WineReviewResponse.builder()
                .name(tastingNote.getMember().getName())
                .review(tastingNote.getReview())
                .rating(tastingNote.getRating())
                .createdAt(tastingNote.getCreatedAt())
                .build();
    }
}
