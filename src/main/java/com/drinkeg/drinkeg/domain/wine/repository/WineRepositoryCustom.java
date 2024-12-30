package com.drinkeg.drinkeg.domain.wine.repository;


import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponseDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponseDTO;

import java.util.List;

public interface WineRepositoryCustom {
    List<WinePreviewResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId);
    WineResponseWithThreeReviewsDTO findWineResponseByWineId(Long wineId, Long memberID);

    List<WineReviewResponseDTO> findWineReviewsByWineIdAndMemberId(Long wineId, boolean orderByLatest);

    List<HomeWineDTO> findRecommendWinesByMember(Member member);

    List<HomeWineDTO> findMostLikedWines();
}
