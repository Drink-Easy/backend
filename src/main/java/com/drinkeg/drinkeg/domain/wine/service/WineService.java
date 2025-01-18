package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.dto.response.*;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.opencsv.exceptions.CsvException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface WineService {

    PageResponse<WinePreviewResponse> searchWinesByName(String searchName, Pageable pageable);

    void updateWineNoteStatics(Long wineId);

    WineWithThreeReviewsResponse getWineInfoWithThreeReviews(Long windId, String username);

    List<WineReviewResponse> getWineReviewsAndIsLikedByWineId(Long wineId, SortType orderByLatest, Pageable pageable);

    List<HomeWineResponse> getRecommendWineList(String username);

    List<HomeWineResponse> getMostLikedWineList();
}
