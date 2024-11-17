package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineReviewResponseDTO;

import java.util.List;

public interface WineRepositoryCustom {
    List<WineReviewResponseDTO> findWineReviewsById(Long wineId);

    WineResponseDTO findWineResponseByWineId(Long wineId);
}
