package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Inquiry, Long> {
}
