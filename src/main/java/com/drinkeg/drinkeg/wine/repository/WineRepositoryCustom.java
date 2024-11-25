package com.drinkeg.drinkeg.wine.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewResponseDTO;

import java.util.List;

public interface WineRepositoryCustom {
    List<WineReviewResponseDTO> findWineReviewsById(Long wineId);

    WineResponseWithThreeReviewsDTO findWineResponseByWineId(Long wineId);
    List<RecommendWineDTO> findRecommendWines(Member member);
    List<SearchWineResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId);
}
