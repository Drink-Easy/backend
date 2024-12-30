package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponseDTO;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;

import java.util.List;
import java.util.Optional;

public interface TastingNoteRepositoryCustom {

    Optional<TastingNoteResponseDTO> findTastingNoteWithWineAndNoseByTastingNoteIdAndUsername(Long tastingNoteId, String username);

    Optional<TastingNote> findTastingNoteWithNoseById(Long tastingNoteId);

    List<TastingNote> findTastingNotesWithWineAndNoseByUsername(String username);
}
