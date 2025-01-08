package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import java.util.List;

public interface TastingNoteRepositoryCustom {

    List<TastingNote> findTastingNoteBySortAndUsername(String sort, String username);

    TastingNoteSortCountResponse findTastingNoteSortCountsByUsername(String username);
}
