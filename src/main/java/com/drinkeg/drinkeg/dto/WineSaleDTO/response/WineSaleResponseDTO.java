package com.drinkeg.drinkeg.dto.WineSaleDTO.response;

import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineStoreDTO.response.WineStoreResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class WineSaleResponseDTO {
    private WineStoreResponseDTO wineStore;
    private int price;
}
