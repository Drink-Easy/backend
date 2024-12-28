package com.drinkeg.drinkeg.domain.wineNote.service;

import com.drinkeg.drinkeg.domain.wineNote.repository.WineNoteRepository;
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
