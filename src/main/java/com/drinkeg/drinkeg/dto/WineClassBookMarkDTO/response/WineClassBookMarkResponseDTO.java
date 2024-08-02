package com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.response;

import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineClassBookMarkResponseDTO {
    private Long id;
    private Long userId;
    private WineClassResponseDTO wineClass;
}
