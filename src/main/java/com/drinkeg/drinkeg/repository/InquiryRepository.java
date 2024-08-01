package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
