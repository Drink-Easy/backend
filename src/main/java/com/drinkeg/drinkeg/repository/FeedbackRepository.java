package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
