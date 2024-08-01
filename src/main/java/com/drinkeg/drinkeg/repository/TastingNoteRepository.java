package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TastingNoteRepository extends JpaRepository<TastingNote, Long> {

    List<TastingNote> findAllByWine(Wine wine);
}
