package com.drinkeg.drinkeg.wine.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WineReviewResponseDTO {

    // 회원이 설정한 이름
    private String name;

    private String review;

    private float satisfaction;


    @QueryProjection
    public WineReviewResponseDTO(String name, String review, float satisfaction) {
        this.name = name;
        this.review = review;
        this.satisfaction = satisfaction;
    }

}
