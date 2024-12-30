package com.drinkeg.drinkeg.wineWishlist.dto.response;

import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class WineWishlistResponseDTO {
    private Long id;
    private SearchWineResponseDTO wine;

    @QueryProjection
    public WineWishlistResponseDTO(Long id, SearchWineResponseDTO wine) {
        this.id = id;
        this. wine = wine;
    }
}
