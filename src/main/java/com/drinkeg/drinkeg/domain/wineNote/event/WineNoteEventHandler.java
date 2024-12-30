package com.drinkeg.drinkeg.domain.wineNote.event;

import com.drinkeg.drinkeg.domain.wineNote.service.WineNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Component
public class WineNoteEventHandler {

    private final WineNoteService wineNoteService;

    @EventListener
    public void handleWineNoteEvent(WineNoteUpdateEvent event) {
        wineNoteService.updateWineNoteStatistics(event.getWineId());
    }

}
