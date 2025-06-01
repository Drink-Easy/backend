package com.drinkeg.drinkeg.domain.wine.wineVintage.repository;

import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;

public interface WineVintageRepositoryCustom {

    WineVintage findByWineIdAndVintageYear(Long wineId, Integer vintageYear);
}
