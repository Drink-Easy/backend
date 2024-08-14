package com.drinkeg.drinkeg.service.wineStoreService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.WineStoreDTO.request.WineStoreRequestDTO;
import com.drinkeg.drinkeg.dto.WineStoreDTO.response.WineStoreResponseDTO;

import java.util.List;

public interface WineStoreService {
    List<WineStoreResponseDTO> getAllWineStores();

    WineStoreResponseDTO getWineStoreById(Long wineStoreId);

    void saveWineStore(WineStoreRequestDTO wineStoreRequestDTO, Member owner);

    WineStoreResponseDTO updateWineStore(Long wineStoreId, WineStoreRequestDTO wineStoreRequestDTO, Member owner);

    void deleteWineStore(Long wineStoreId, Member owner);
}
