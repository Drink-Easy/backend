package com.drinkeg.drinkeg.dto.WineWishlistDTO.response;

import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineWishlistResponseDTO {
    private Long id;
    private SearchWineResponseDTO wine;
}
