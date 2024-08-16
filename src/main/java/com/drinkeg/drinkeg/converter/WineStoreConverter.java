package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineStore;
import com.drinkeg.drinkeg.dto.WineStoreDTO.request.WineStoreRequestDTO;
import com.drinkeg.drinkeg.dto.WineStoreDTO.response.WineStoreResponseDTO;

public class WineStoreConverter {
    public static WineStoreResponseDTO toWineStoreResponseDTO(WineStore wineStore) {
        return WineStoreResponseDTO.builder()
                .id(wineStore.getId())
                .name(wineStore.getName())
                .address(wineStore.getAddress())
                .build();
    }

    public static WineStore toWineStore(WineStoreRequestDTO wineStoreRequestDTO, Member owner) {
        return WineStore.builder()
                .name(wineStoreRequestDTO.getName())
                .address(wineStoreRequestDTO.getAddress())
                .owner(owner)
                .build();
    }
}
