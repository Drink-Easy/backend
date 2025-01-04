package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineReviewResponse {

    // 회원이 설정한 이름
    private String name;

    private String review;

    private float rating;

    private LocalDateTime createdAt;


    @QueryProjection
    public WineReviewResponse(String name, String review, float rating, LocalDateTime createdAt) {
        this.name = name;
        this.review = review;
        this.rating = rating;
        this.createdAt = createdAt;

    }

}
