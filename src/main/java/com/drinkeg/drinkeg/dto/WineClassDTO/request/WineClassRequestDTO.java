package com.drinkeg.drinkeg.dto.WineClassDTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineClassRequestDTO {
    private String title;
    private String video;
    private String description;
}
