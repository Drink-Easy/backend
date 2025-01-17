package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.repository.dto.WineNoteStatisticsAvgDto;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TastingNoteRepositoryCustom {

    List<TastingNote> findAllTastingNoteBy(Long wineId, SortType sort, Pageable pageable);

    WineNoteStatisticsAvgDto findWineNoteStatisticsByWineId(Long wineId);

    List<String> findTopThreeNoseByWineId(Long wineId);

    List<TastingNote> findTastingNoteBySortAndUsername(TastingNoteWineSort wineSort, String username);

    TastingNoteSortCountResponse findTastingNoteSortCountsByUsername(String username);
}
