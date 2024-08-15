package com.drinkeg.drinkeg.dto.WineDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineReviewResponseDTO {

    private float satisfaction;

    private String review;

}
