package com.drinkeg.drinkeg.domain.wine.wineVintage.service;

import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.dto.WineNoteStatisticsAvgDto;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wine.wineVintage.repository.WineVintageRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WineVintageServiceImpl implements WineVintageService{

    private final WineVintageRepository wineVintageRepository;
    private final TastingNoteRepository tastingNoteRepository;

    @Override
    public void updateWineVintageNoteStatics(Long wineVintageId) {
        WineVintage wineVintage = getWineVintageById(wineVintageId);

        WineNoteStatisticsAvgDto avgDto = tastingNoteRepository.findWineVintageStatisticsByWineVintageId(wineVintageId);
        List<String> topThreeNose = tastingNoteRepository.findTopThreeNoseByWineId(wineVintageId);

        wineVintage.getWine().getWineNoteStatistics()
                .updateAvgStatistics(avgDto)
                .updateNose(topThreeNose);
    }

    private WineVintage getWineVintageById(Long wineVintageId) {
        return wineVintageRepository.findById(wineVintageId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_VINTAGE_NOT_FOUND));
    }

}
