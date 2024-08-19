package com.drinkeg.drinkeg.service.wineSaleService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineSaleConverter;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineSale;
import com.drinkeg.drinkeg.domain.WineStore;
import com.drinkeg.drinkeg.dto.WineSaleDTO.request.WineSaleRequestDTO;
import com.drinkeg.drinkeg.dto.WineSaleDTO.response.WineSaleResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineRepository;
import com.drinkeg.drinkeg.repository.WineSaleRepository;
import com.drinkeg.drinkeg.repository.WineStoreRepository;
import com.drinkeg.drinkeg.service.wineService.WineService;
import com.drinkeg.drinkeg.service.wineStoreService.WineStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineSaleServiceImpl implements WineSaleService {
    private final WineSaleRepository wineSaleRepository;
    private final WineStoreRepository wineStoreRepository;
    private final WineRepository wineRepository;

    @Override
    public List<WineSaleResponseDTO> getAllWineSales() {
        return wineSaleRepository.findAll().stream()
                .map(WineSaleConverter::toWineSaleResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WineSaleResponseDTO getWineSaleById(Long wineSaleId) {
        return WineSaleConverter.toWineSaleResponseDTO(
                wineSaleRepository.findById(wineSaleId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_SALE_NOT_FOUND)));
    }

    @Override
    public List<WineSaleResponseDTO> getAllWineSalesByWineStore(Long wineStoreId) {
        WineStore wineStore = wineStoreRepository.findById(wineStoreId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_STORE_NOT_FOUND));

        return wineSaleRepository.findAllByWineStore(wineStore).stream()
                .map(WineSaleConverter::toWineSaleResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WineSaleResponseDTO> getAllWineSalesByWine(Long wineId) {
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        return wineSaleRepository.findAllByWine(wine).stream()
                .map(WineSaleConverter::toWineSaleResponseDTO)
                .collect(Collectors.toList());
    }


    @Override
    public WineSaleResponseDTO createWineSale(WineSaleRequestDTO wineSaleRequestDTO) {
        Wine wine = wineRepository.findById(wineSaleRequestDTO.getWineId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));
        WineStore wineStore = wineStoreRepository.findById(wineSaleRequestDTO.getWineStoreId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_STORE_NOT_FOUND));

        WineSale wineSale = WineSaleConverter.toWinSale(wineSaleRequestDTO, wine, wineStore);
        return WineSaleConverter.toWineSaleResponseDTO(
                wineSaleRepository.save(wineSale));
    }

    @Override
    public WineSaleResponseDTO updateWineSale(Long wineSaleId, WineSaleRequestDTO wineSaleRequestDTO) {
        WineSale wineSale = wineSaleRepository.findById(wineSaleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_SALE_NOT_FOUND));
        Wine wine = wineRepository.findById(wineSaleRequestDTO.getWineId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));
        WineStore wineStore = wineStoreRepository.findById(wineSaleRequestDTO.getWineStoreId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_STORE_NOT_FOUND));

        wineSale.updatePrice(wineSaleRequestDTO.getPrice())
                .updateWine(wine)
                .updateWineStore(wineStore);
        return WineSaleConverter.toWineSaleResponseDTO(wineSale);
    }

    @Override
    public void deleteWineSaleById(Long wineSaleId) {
        if (!wineSaleRepository.existsById(wineSaleId))
            throw new GeneralException(ErrorStatus.WINE_SALE_NOT_FOUND);

        wineSaleRepository.deleteById(wineSaleId);
    }
}
