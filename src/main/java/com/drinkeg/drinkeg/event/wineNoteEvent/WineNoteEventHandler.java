package com.drinkeg.drinkeg.event.wineNoteEvent;

import com.drinkeg.drinkeg.repository.WineNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Component
public class WineNoteEventHandler {

    private final WineNoteRepository wineNoteRepository;

    @EventListener
    public void handleWineNoteEvent(WineNoteUpdateEvent event) {
        wineNoteRepository.updateWineNoteStatistics(event.getWineId());
    }

}
