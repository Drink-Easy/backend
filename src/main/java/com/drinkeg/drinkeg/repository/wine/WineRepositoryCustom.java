package com.drinkeg.drinkeg.repository.wine;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineReviewResponseDTO;

import java.util.List;

public interface WineRepositoryCustom {
    List<WineReviewResponseDTO> findWineReviewsById(Long wineId);

    WineResponseDTO findWineResponseByWineId(Long wineId);
    List<RecommendWineDTO> findRecommendWines(Member member);
    List<SearchWineResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId);
}
