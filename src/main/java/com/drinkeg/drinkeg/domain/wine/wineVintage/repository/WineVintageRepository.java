package com.drinkeg.drinkeg.domain.wine.wineVintage.repository;

import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WineVintageRepository extends JpaRepository<WineVintage, Long>, WineVintageRepositoryCustom {
}
