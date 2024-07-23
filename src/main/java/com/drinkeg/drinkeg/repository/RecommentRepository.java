package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Recomment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommentRepository extends JpaRepository<Recomment, Long> {
}
