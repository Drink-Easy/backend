package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import java.util.List;
import java.util.Optional;

public interface TastingNoteRepositoryCustom {

    List<TastingNote> findTastingNoteBySortAndUsername(TastingNoteWineSort wineSort, String username);

    TastingNoteSortCountResponse findTastingNoteSortCountsByUsername(String username);
}
