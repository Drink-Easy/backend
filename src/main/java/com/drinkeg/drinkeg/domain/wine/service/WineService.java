package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WineService {

    PageResponse<WinePreviewResponse> searchWinesByName(String searchName, Pageable pageable);

    void updateWineNoteStatics(Long wineId);

    WineWithThreeReviewsResponse getWineInfoWithThreeReviews(Long windId, String username);

    PageResponse<WineReviewResponse> getWineReviewsAndIsLikedByWineId(Long wineId, SortType orderByLatest, Pageable pageable);

    List<HomeWineResponse> getRecommendWineList(String username);

    List<HomeWineResponse> getMostLikedWineList();
}
