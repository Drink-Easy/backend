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

    private float satisfaction;

    private LocalDateTime createdAt;


    @QueryProjection
    public WineReviewDTO(String name, String review, float satisfaction, LocalDateTime createdAt) {
        this.name = name;
        this.review = review;
        this.satisfaction = satisfaction;
        this.createdAt = createdAt;

    }

}
