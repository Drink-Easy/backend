package com.drinkeg.drinkeg.domain.wine.repository;


import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.drinkeg.drinkeg.domain.wine.repository.dto.WineNoteStatisticsAvgDto;

import java.util.List;

public interface WineRepositoryCustom {

    List<WineReviewResponse> findWineReviewsByWineIdAndMemberId(Long wineId, boolean orderByLatest);

    List<HomeWineResponse> findRecommendWinesByMember(Member member);

    List<HomeWineResponse> findMostLikedWines();

    WineNoteStatisticsAvgDto findWineNoteStatisticsByWineId(Long wineId);

    List<String> findTopThreeNoseByWineId(Long wineId);
}
