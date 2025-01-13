package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.opencsv.exceptions.CsvException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface WineService {

    public List<WinePreviewResponse> searchWinesByName(String searchName, Pageable pageable);

    public void updateWineNoteStatics(Long wineId);

    public WineWithThreeReviewsResponse getWineInfoWithThreeReviews(Long windId, String username);

    public List<WineReviewResponse> getWineReviewsAndIsLikedByWineId(Long wineId, SortType orderByLatest);

    public List<HomeWineResponse> getRecommendWineList(String username);

    public List<HomeWineResponse> getMostLikedWineList();
}
