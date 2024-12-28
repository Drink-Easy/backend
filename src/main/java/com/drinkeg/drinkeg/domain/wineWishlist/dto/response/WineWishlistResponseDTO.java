package com.drinkeg.drinkeg.domain.wineWishlist.dto.response;

import com.drinkeg.drinkeg.domain.wine.dto.response.SearchWineResponseDTO;
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
