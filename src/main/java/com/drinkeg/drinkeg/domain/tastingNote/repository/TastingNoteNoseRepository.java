package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TastingNoteNoseRepository extends JpaRepository<TastingNoteNose, Long>, TastingNoteNoseRepositoryCustom {


}
