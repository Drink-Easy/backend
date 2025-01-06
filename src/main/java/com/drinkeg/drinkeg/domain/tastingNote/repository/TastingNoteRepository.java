package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteRepository extends JpaRepository<TastingNote, Long>, TastingNoteRepositoryCustom {

}
