package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.WineNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineNoteRepository extends JpaRepository<WineNote, Long> {
}
