package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.WineNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineNewsRepository extends JpaRepository<WineNews, Long> {
}
