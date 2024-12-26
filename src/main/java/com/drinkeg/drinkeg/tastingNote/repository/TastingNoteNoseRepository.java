package com.drinkeg.drinkeg.tastingNote.repository;

import com.drinkeg.drinkeg.tastingNote.domain.TastingNoteNose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteNoseRepository extends JpaRepository<TastingNoteNose, Long>, TastingNoteNoseRepositoryCustom {
}
