package com.drinkeg.drinkeg.domain.wine.repository;


import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WineRepositoryCustom {
    List<Wine> findRecommendWinesBy(List<String> wineArea, List<String> wineSort, Long price);

    List<Wine> findMostLikedWines();

    List<Wine> searchByName(String searchName, Pageable pageable);

    List<Wine> searchByNameSortVarietyAndArea(String searchName, String wineSort, String wineVariety, String wineArea, Pageable pageable);

    long countSearchWine(String searchName);

    long countSearchWineSortVarietyAndArea(String searchName, String wineSort, String wineVariety, String wineCountry);
}
