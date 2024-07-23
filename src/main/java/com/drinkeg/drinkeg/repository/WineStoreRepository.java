package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.WineStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineStoreRepository extends JpaRepository<WineStore, Long> {
}

