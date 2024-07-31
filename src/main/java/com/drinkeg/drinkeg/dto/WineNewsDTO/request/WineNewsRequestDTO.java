package com.drinkeg.drinkeg.dto.WineNewsDTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineNewsRequestDTO {
    private String title;
    private String cardNewsImg;
}
