package com.drinkeg.drinkeg.tastingNote.repository;

import com.drinkeg.drinkeg.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.tastingNote.dto.response.TastingNoteResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TastingNoteRepositoryCustom {

    Optional<TastingNoteResponseDTO> findTastingNoteWithWineAndNoseByTastingNoteIdAndUsername(Long tastingNoteId, String username);

    Optional<TastingNote> findTastingNoteWithNoseById(Long tastingNoteId);

    List<TastingNote> findTastingNotesWithWineAndNoseByUsername(String username);
}
