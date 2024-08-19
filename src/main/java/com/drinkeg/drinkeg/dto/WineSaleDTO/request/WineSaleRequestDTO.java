package com.drinkeg.drinkeg.dto.WineSaleDTO.request;

import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WineSaleRequestDTO {
    private Long wineId;
    private Long wineStoreId;
    private int price;
}
