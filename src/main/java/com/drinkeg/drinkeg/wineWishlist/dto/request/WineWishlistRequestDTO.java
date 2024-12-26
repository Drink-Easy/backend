package com.drinkeg.drinkeg.wineWishlist.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineWishlistRequestDTO {
    private long wineId;
}
