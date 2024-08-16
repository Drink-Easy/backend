package com.drinkeg.drinkeg.dto.WineDTO.response;

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

}
