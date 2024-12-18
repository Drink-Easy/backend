package com.drinkeg.drinkeg.wineWishlist.dto.response;

import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
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
