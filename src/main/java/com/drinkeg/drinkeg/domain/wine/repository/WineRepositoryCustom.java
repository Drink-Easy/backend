package com.drinkeg.drinkeg.domain.wine.repository;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewDTO;

import java.util.List;

public interface WineRepositoryCustom {
    List<SearchWineResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId);
    WineResponseWithThreeReviewsDTO findWineResponseByWineId(Long wineId, Long memberID);

    List<WineReviewDTO> findWineReviewsByWineIdAndMemberId(Long wineId, boolean orderByLatest);

    List<RecommendWineDTO> findRecommendWines(Member member);
}
