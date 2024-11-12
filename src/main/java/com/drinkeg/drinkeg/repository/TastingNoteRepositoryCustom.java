package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.TastingNote;

import java.util.Optional;

public interface TastingNoteRepositoryCustom {

    Optional<TastingNote> findByIdWithWineAndNose(Long tastingNoteId);
}
