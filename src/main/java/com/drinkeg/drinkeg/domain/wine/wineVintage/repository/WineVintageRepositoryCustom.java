package com.drinkeg.drinkeg.domain.wine.wineVintage.repository;

import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;

import java.util.Optional;

public interface WineVintageRepositoryCustom {

    WineVintage findByWineIdAndVintageYearFetch(Long wineId, int vintageYear);
}
