package com.drinkeg.drinkeg.tastingNote.repository;

import com.drinkeg.drinkeg.tastingNote.domain.TastingNote;

import java.util.List;
import java.util.Optional;

public interface TastingNoteRepositoryCustom {

    Optional<TastingNote> findTastingNoteWithWineAndNoseById(Long tastingNoteId);

    Optional<TastingNote> findTastingNoteWithNoseById(Long tastingNoteId);

    List<TastingNote> findTastingNotesWithWineAndNoseByUsername(String username);
}
