package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.WineClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineClassRepository extends JpaRepository<WineClass, Long> {
}
