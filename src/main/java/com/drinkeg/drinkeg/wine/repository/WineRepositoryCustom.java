package com.drinkeg.drinkeg.wine.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewResponseDTO;

import java.util.List;

public interface WineRepositoryCustom {
    List<SearchWineResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId);

    WineResponseWithThreeReviewsDTO findWineResponseByWineId(Long wineId, Long memberID);
    WineReviewResponseDTO findWineReviewsAndLikeStatusByWineIdAndMemberId(Long wineId, Long memberId);

    List<RecommendWineDTO> findRecommendWines(Member member);
}
