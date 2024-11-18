package com.drinkeg.drinkeg.service.wineNoteService;

import com.drinkeg.drinkeg.repository.wineNote.WineNoteRepository;
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
