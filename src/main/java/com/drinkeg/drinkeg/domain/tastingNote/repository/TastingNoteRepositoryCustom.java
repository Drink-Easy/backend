package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.repository.dto.WineNoteStatisticsAvgDto;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import java.util.List;
import java.util.Optional;

public interface TastingNoteRepositoryCustom {

    Optional<TastingNote> findTastingNoteWithNoseById(Long tastingNoteId);

    List<TastingNote> findAllTastingNoteBy(Long wineId, SortType sort);

    WineNoteStatisticsAvgDto findWineNoteStatisticsByWineId(Long wineId);

    List<String> findTopThreeNoseByWineId(Long wineId);

    List<TastingNote> findTastingNoteBySortAndUsername(TastingNoteWineSort wineSort, String username);

    TastingNoteSortCountResponse findTastingNoteSortCountsByUsername(String username);
}
