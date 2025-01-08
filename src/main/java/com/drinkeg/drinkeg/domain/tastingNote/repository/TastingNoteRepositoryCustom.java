package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import java.util.List;

public interface TastingNoteRepositoryCustom {

    List<TastingNote> findTastingNoteBySortAndUsername(String sort, String username);
}
