package com.drinkeg.drinkeg.service.wineStoreService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineClassConverter;
import com.drinkeg.drinkeg.converter.WineStoreConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineStore;
import com.drinkeg.drinkeg.dto.WineStoreDTO.request.WineStoreRequestDTO;
import com.drinkeg.drinkeg.dto.WineStoreDTO.response.WineStoreResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineClassRepository;
import com.drinkeg.drinkeg.repository.WineStoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineStoreServiceImpl implements WineStoreService {
    private final WineStoreRepository wineStoreRepository;
    private final WineClassRepository wineClassRepository;

    @Override
    public List<WineStoreResponseDTO> getAllWineStores() {
        return wineStoreRepository.findAll()
                .stream()
                .map(WineStoreConverter::toWineStoreResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WineStoreResponseDTO getWineStoreById(Long wineStoreId) {
        return WineStoreConverter.toWineStoreResponseDTO(
                wineStoreRepository.findById(wineStoreId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_STORE_NOT_FOUND))
        );
    }

    @Override
    public void saveWineStore(WineStoreRequestDTO wineStoreRequestDTO, Member owner) {
        wineStoreRepository.save(
                WineStoreConverter.toWineStore(wineStoreRequestDTO, owner)
        );
    }

    @Override
    @Transactional
    public WineStoreResponseDTO updateWineStore(Long wineStoreId, WineStoreRequestDTO wineStoreRequestDTO, Member owner) {
        WineStore wineStore = wineStoreRepository.findById(wineStoreId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_STORE_NOT_FOUND));
        // 유저 권한 확인
        if (!wineStore.getOwner().equals(owner) && !owner.getRole().equals("ROLE_ADMIN"))
            throw new GeneralException(ErrorStatus.WINE_STORE_UNAUTHORIZED);

        return WineStoreConverter.toWineStoreResponseDTO(
                wineStore.updateAddress(wineStoreRequestDTO.getAddress())
                        .updateName(wineStoreRequestDTO.getName()));
    }

    @Override
    public void deleteWineStore(Long wineStoreId, Member owner) {
        WineStore wineStore = wineStoreRepository.findById(wineStoreId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_STORE_NOT_FOUND));
        // 유저 권한 확인
        if (!wineStore.getOwner().equals(owner) && !owner.getRole().equals("ROLE_ADMIN"))
            throw new GeneralException(ErrorStatus.WINE_STORE_UNAUTHORIZED);

        wineStoreRepository.deleteById(wineStoreId);
    }
}
