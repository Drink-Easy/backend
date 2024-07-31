package com.drinkeg.drinkeg.dto.WineNewsDTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineNewsResponseDTO {
    private Long id;
    private String title;
    private String cardNewsImg;
}
