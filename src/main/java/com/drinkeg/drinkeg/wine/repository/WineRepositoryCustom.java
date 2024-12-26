package com.drinkeg.drinkeg.wine.repository;

import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewDTO;

import java.util.List;

public interface WineRepositoryCustom {
    List<SearchWineResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId);
    WineResponseWithThreeReviewsDTO findWineResponseByWineId(Long wineId, Long memberID);

    List<WineReviewDTO> findWineReviewsByWineIdAndMemberId(Long wineId, boolean orderByLatest);

    List<RecommendWineDTO> findRecommendWines(Member member);
}
