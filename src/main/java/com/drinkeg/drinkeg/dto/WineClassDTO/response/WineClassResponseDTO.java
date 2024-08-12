package com.drinkeg.drinkeg.dto.WineClassDTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineClassResponseDTO {
    private Long id;
    private String title;
    private String video;
    private String description;
}
