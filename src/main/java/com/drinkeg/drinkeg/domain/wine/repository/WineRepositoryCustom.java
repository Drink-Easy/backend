package com.drinkeg.drinkeg.domain.wine.repository;


import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WineRepositoryCustom {
    List<Wine> findRecommendWinesBy(List<String> wineArea, List<String> wineSort, Long price);

    List<Wine> findMostLikedWines();

    Page<Wine> searchByNameWithPaging(String searchName, Pageable pageable);
}
