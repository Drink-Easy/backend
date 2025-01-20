package com.drinkeg.drinkeg.domain.wine.repository;


import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WineRepositoryCustom {
    List<Wine> findRecommendWinesBy(List<String> wineArea, List<String> wineSort, Long price);

    List<Wine> findMostLikedWines();

    List<Wine> searchByName(String searchName, Pageable pageable);

    long countSearchWinePage(String searchName);
}
