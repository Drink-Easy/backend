package com.drinkeg.drinkeg.domain.wineClass.repository;

import com.drinkeg.drinkeg.domain.wineClass.domain.WineClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineClassRepository extends JpaRepository<WineClass, Long>, WineClassRepositoryCustom {

}
