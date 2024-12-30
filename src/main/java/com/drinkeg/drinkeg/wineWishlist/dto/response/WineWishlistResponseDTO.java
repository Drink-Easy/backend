package com.drinkeg.drinkeg.wineWishlist.dto.response;

import com.drinkeg.drinkeg.wine.dto.response.WinePreviewResponseDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class WineWishlistResponseDTO {
    private Long id;
    private WinePreviewResponseDTO wine;

    @QueryProjection
    public WineWishlistResponseDTO(Long id, WinePreviewResponseDTO wine) {
        this.id = id;
        this. wine = wine;
    }
}
