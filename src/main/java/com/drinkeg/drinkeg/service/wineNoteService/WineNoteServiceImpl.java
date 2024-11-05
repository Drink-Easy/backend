package com.drinkeg.drinkeg.service.wineNoteService;

import com.drinkeg.drinkeg.event.WineNoteUpdateEvent;
import com.drinkeg.drinkeg.repository.WineNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WineNoteServiceImpl implements WineNoteService {

    private final WineNoteRepository wineNoteRepository;

    @EventListener
    public void handleWineNoteEvent(WineNoteUpdateEvent event) {
        wineNoteRepository.updateWineNoteStatistics(event.getWineId());
    }

}
