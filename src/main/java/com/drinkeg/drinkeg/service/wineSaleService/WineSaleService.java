package com.drinkeg.drinkeg.service.wineSaleService;

import com.drinkeg.drinkeg.dto.WineSaleDTO.request.WineSaleRequestDTO;
import com.drinkeg.drinkeg.dto.WineSaleDTO.response.WineSaleResponseDTO;

import java.util.List;

public interface WineSaleService {
    List<WineSaleResponseDTO> getAllWineSales();

    WineSaleResponseDTO getWineSaleById(Long wineSaleId);

    List<WineSaleResponseDTO> getAllWineSalesByWineStore(Long wineStoreId);

    List<WineSaleResponseDTO> getAllWineSalesByWine(Long wineId);

    WineSaleResponseDTO createWineSale(WineSaleRequestDTO wineSaleRequestDTO);

    WineSaleResponseDTO updateWineSale(Long wineSaleId, WineSaleRequestDTO wineSaleRequestDTO);

    void deleteWineSaleById(Long wineSaleId);

}
