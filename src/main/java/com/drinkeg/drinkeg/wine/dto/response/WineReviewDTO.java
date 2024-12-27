package com.drinkeg.drinkeg.wine.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WineReviewDTO {

    // 회원이 설정한 이름
    private String name;

    private String review;

    private float rating;

    private LocalDateTime createdAt;


    @QueryProjection
    public WineReviewDTO(String name, String review, float rating, LocalDateTime createdAt) {
        this.name = name;
        this.review = review;
        this.rating = rating;
        this.createdAt = createdAt;

    }

}
