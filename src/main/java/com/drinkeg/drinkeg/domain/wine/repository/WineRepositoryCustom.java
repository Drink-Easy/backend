package com.drinkeg.drinkeg.domain.wine.repository;


import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;

import java.util.List;

public interface WineRepositoryCustom {
    List<Wine> findRecommendWinesBy(List<String> wineArea, List<String> wineSort, Long price);

    List<HomeWineResponse> findMostLikedWines();
}
