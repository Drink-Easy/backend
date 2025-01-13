package com.drinkeg.drinkeg.domain.wineWishlist.dto.response;

import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineWishlistResponse {
    private Long id;
    private WinePreviewResponse wine;

    @QueryProjection
    public WineWishlistResponse(Long id, WinePreviewResponse wine) {
        this.id = id;
        this. wine = wine;
    }
}
