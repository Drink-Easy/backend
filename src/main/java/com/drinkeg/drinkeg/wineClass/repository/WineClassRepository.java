package com.drinkeg.drinkeg.wineClass.repository;

import com.drinkeg.drinkeg.wineClass.domain.WineClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineClassRepository extends JpaRepository<WineClass, Long>, WineClassRepositoryCustom {

}
