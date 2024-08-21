package com.drinkeg.drinkeg.converter;


import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineSale;
import com.drinkeg.drinkeg.domain.WineStore;
import com.drinkeg.drinkeg.dto.WineSaleDTO.request.WineSaleRequestDTO;
import com.drinkeg.drinkeg.dto.WineSaleDTO.response.WineSaleResponseDTO;

public class WineSaleConverter {
    public static WineSale toWinSale(WineSaleRequestDTO wineSaleRequestDTO, Wine wine, WineStore wineStore) {
        return WineSale.builder()
                .wine(wine)
                .wineStore(wineStore)
                .price(wineSaleRequestDTO.getPrice())
                .build();
    }

    public static WineSaleResponseDTO toWineSaleResponseDTO(WineSale wineSale) {
        return WineSaleResponseDTO.builder()
                .wineStore(WineStoreConverter.toWineStoreResponseDTO(wineSale.getWineStore()))
                .price(wineSale.getPrice())
                .build();
    }
}
