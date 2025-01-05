package com.drinkeg.drinkeg.domain.wine.repository;


import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;

import java.util.List;

public interface WineRepositoryCustom {
    WineWithThreeReviewsResponse findWineResponseByWineId(Long wineId, Long memberID);

    List<WineReviewResponse> findWineReviewsByWineIdAndMemberId(Long wineId, boolean orderByLatest);

    List<HomeWineResponse> findRecommendWinesByMember(Member member);

    List<HomeWineResponse> findMostLikedWines();
}
