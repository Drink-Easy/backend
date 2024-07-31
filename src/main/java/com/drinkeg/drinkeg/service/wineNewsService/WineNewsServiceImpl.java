package com.drinkeg.drinkeg.service.wineNewsService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineNewsConverter;
import com.drinkeg.drinkeg.dto.WineNewsDTO.request.WineNewsRequestDTO;
import com.drinkeg.drinkeg.dto.WineNewsDTO.response.WineNewsResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineNewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineNewsServiceImpl implements WineNewsService {
    private final WineNewsRepository wineNewsRepository;

    @Override
    public List<WineNewsResponseDTO> getAllWineNewses() {
        return wineNewsRepository.findAll()
                .stream()
                .map(WineNewsConverter::toWineNewsResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WineNewsResponseDTO getWineNewsById(Long WineNewsId) {
        return WineNewsConverter.toWineNewsResponseDTO(
                wineNewsRepository.findById(WineNewsId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINENEWS_NOT_FOUND)));
    }

    @Override
    public void saveWineNews(WineNewsRequestDTO WineNewsRequestDTO) {
        wineNewsRepository.save(
                WineNewsConverter.toWineNews(WineNewsRequestDTO));
    }

    @Override
    @Transactional
    public WineNewsResponseDTO updateWineNews(Long WineNewsId, WineNewsRequestDTO WineNewsRequestDTO) {
        return WineNewsConverter.toWineNewsResponseDTO(
                wineNewsRepository.findById(WineNewsId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINENEWS_NOT_FOUND))
                        .updateTitle(WineNewsRequestDTO.getTitle())
                        .updateCardNewsImg(WineNewsRequestDTO.getCardNewsImg()));
    }

    @Override
    public void deleteWineNews(Long WineNewsId) {
        wineNewsRepository.deleteById(WineNewsId);
    }
}
