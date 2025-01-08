package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import java.util.List;
import java.util.Optional;

public interface TastingNoteRepositoryCustom {

    Optional<TastingNote> findTastingNoteWithWineAndNoseAndMemberById(Long noteId);

    List<TastingNote> findTastingNoteBySortAndUsername(String sort, String username);

    TastingNoteSortCountResponse findTastingNoteSortCountsByUsername(String username);
}
