package com.drinkeg.drinkeg.dto.WineNewsBookMarkDTO.response;

import com.drinkeg.drinkeg.dto.WineNewsDTO.response.WineNewsResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineNewsBookMarkResponseDTO {
    private Long id;
    private Long userId;
    private WineNewsResponseDTO wineNews;
}
