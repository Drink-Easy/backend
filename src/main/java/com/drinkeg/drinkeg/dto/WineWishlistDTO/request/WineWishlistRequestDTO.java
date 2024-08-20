package com.drinkeg.drinkeg.dto.WineWishlistDTO.request;

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
