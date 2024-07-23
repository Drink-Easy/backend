package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
