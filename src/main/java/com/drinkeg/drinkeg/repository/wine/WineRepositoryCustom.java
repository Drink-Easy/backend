package com.drinkeg.drinkeg.repository.wine;

import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineReviewResponseDTO;

import java.util.List;

public interface WineRepositoryCustom {
    List<WineReviewResponseDTO> findWineReviewsById(Long wineId);

    WineResponseDTO findWineResponseByWineId(Long wineId);

    List<SearchWineResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId);


}
