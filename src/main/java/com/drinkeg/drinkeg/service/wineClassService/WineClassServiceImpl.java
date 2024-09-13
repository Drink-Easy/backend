package com.drinkeg.drinkeg.service.wineClassService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineClassConverter;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineClassRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineClassServiceImpl implements WineClassService {
    private final WineClassRepository wineClassRepository;

    @Override
    public List<WineClassResponseDTO> getAllWineClasses() {
        return wineClassRepository.findAll()
                .stream()
                .map(WineClassConverter::toWineClassResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WineClassResponseDTO getWineClassById(Long wineClassId) {
        return WineClassConverter.toWineClassResponseDTO(
                wineClassRepository.findById(wineClassId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND)));
    }

    @Override
    public void saveWineClass(WineClassRequestDTO wineClassRequestDTO) {
        wineClassRepository.save(
                WineClassConverter.toWineClass(wineClassRequestDTO));
    }

    @Override
    @Transactional
    public WineClassResponseDTO updateWineClass(Long wineClassId, WineClassRequestDTO wineClassRequestDTO) {
        return WineClassConverter.toWineClassResponseDTO(
                wineClassRepository.findById(wineClassId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND))
                        .updateTitle(wineClassRequestDTO.getTitle())
                        .updateThumbnail(wineClassRequestDTO.getThumbnailUrl())
                        .updateContent(wineClassRequestDTO.getContent()));
    }

    @Override
    public void deleteWineClass(Long wineClassId) {
        if (!wineClassRepository.existsById(wineClassId))
            throw new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND);
        wineClassRepository.deleteById(wineClassId);
    }

    @Override
    public WineClass findWineClassById(Long wineClassId) {
        return wineClassRepository.findById(wineClassId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));
    }
}
