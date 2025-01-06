package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;

import java.io.IOException;
import java.util.List;

public interface WineService {

    public List<WinePreviewResponse> searchWinesByName(String searchName);

    public void updateWineNoteStatics(Long wineId);

    public WineWithThreeReviewsResponse getWineResponseByWineId(Long wineId, String username);

    public List<WineReviewResponse> getWineReviewsAndIsLikedByWineId(Long wineId, boolean orderByLatest);

    public List<HomeWineResponse> getRecommendWineList(String username);

    public List<HomeWineResponse> getMostLikedWineList();

    public void uploadWineImage() throws IOException;

}
