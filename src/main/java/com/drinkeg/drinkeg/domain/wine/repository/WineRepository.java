package com.drinkeg.drinkeg.domain.wine.repository;


import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WineRepository extends JpaRepository<Wine, Long>, WineRepositoryCustom {
}