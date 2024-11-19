package com.drinkeg.drinkeg.tastingNote.repository;

import com.drinkeg.drinkeg.tastingNote.domain.TastingNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TastingNoteRepository extends JpaRepository<TastingNote, Long>, TastingNoteRepositoryCustom {

}
