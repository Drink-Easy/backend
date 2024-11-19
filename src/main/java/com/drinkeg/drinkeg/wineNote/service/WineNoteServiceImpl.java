package com.drinkeg.drinkeg.wineNote.service;

import com.drinkeg.drinkeg.wineNote.dao.WineNoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class WineNoteServiceImpl implements WineNoteService{
    private final WineNoteRepository wineNoteRepository;

    @Override
    public void updateWineNoteStatistics(Long wineId) {
        wineNoteRepository.updateWineNoteStatistics(wineId);
    }
}
