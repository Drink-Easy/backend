package com.drinkeg.drinkeg.wine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineReviewResponseDTO {

    // 회원이 설정한 이름
    private String name;

    private float satisfaction;

    private String review;

    // QueryDSL에서 사용할 수 있는 생성자 추가
    public WineReviewResponseDTO(String name, String review, Float satisfaction) {
        this.name = name;
        this.review = review;
        this.satisfaction = satisfaction;
    }

}
