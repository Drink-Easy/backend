package com.drinkeg.drinkeg.wine.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WineReviewDTO {

    // 회원이 설정한 이름
    private String name;

    private String review;

    private float satisfaction;


    @QueryProjection
    public WineReviewDTO(String name, String review, float satisfaction) {
        this.name = name;
        this.review = review;
        this.satisfaction = satisfaction;
    }

}
