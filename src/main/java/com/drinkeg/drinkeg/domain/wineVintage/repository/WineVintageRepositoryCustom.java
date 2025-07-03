package com.drinkeg.drinkeg.domain.wineVintage.repository;

import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;

public interface WineVintageRepositoryCustom {

    WineVintage findByWineIdAndVintageYear(Long wineId, Integer vintageYear);
}
