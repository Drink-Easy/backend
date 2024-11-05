package com.drinkeg.drinkeg.service.wineNoteService;

import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.repository.WineNoteRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WineNoteServiceImpl implements WineNoteService {

    private final WineNoteRepository wineNoteRepository;

    @Override
    public void updateWineNote(Long wineId) {

        wineNoteRepository.updateWineNoteStatistics(wineId);

    }
}
