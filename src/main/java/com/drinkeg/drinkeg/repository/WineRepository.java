package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineRepository extends JpaRepository<Wine, Long> {
}
