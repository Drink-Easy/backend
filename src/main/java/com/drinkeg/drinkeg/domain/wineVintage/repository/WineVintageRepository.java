package com.drinkeg.drinkeg.domain.wineVintage.repository;

import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineVintageRepository extends JpaRepository<WineVintage, Long>, WineVintageRepositoryCustom {
}
