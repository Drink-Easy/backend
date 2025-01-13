package com.drinkeg.drinkeg.domain.wine.event;

import com.drinkeg.drinkeg.domain.tastingNote.event.TastingNoteUpdateEvent;
import com.drinkeg.drinkeg.domain.wine.service.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WineEventHandler {
    private final WineService wineService;

    @EventListener
    public void handleTastingNoteUpdateEvent(TastingNoteUpdateEvent event) {
        wineService.updateWineNoteStatics(event.getWineId());
    }
}
