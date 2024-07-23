package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.TastingNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteRepository extends JpaRepository<TastingNote, Long> {
}
