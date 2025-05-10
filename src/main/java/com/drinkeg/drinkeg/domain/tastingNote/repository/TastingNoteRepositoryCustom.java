package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.dto.WineNoteStatisticsAvgDto;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TastingNoteRepositoryCustom {

    List<TastingNote> findAllTastingNoteBy(Long wineId, SortType sort, Pageable pageable);

    long countTastingNoteByWineId(Long wineId);

    WineNoteStatisticsAvgDto findWineStatisticsByWineId(Long wineId);
    WineNoteStatisticsAvgDto findWineVintageStatisticsByWineVintageId(Long wineVintageId);

    List<String> findTopThreeNoseByWineId(Long wineId);
    List<String> findTopThreeNoseByWineVintageId(Long wineVintageId);

    List<TastingNote> findTastingNoteBySortAndUsername(TastingNoteWineSort wineSort, String username, Pageable pageable);

    long countTastingNoteBySortAndUsername(TastingNoteWineSort wineSort, String username);

    TastingNoteSortCountResponse findTastingNoteSortCountsByUsername(String username);

    List<TastingNote> searchTastingNoteByWineName(String searchName, String username, Pageable pageable);

    long countSearchTastingNoteByWineName(String searchName, String username);
}
