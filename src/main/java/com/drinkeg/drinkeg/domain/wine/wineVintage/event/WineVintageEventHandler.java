package com.drinkeg.drinkeg.domain.wine.wineVintage.event;

import com.drinkeg.drinkeg.domain.wine.wineVintage.service.WineVintageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WineVintageEventHandler {

    private final WineVintageService wineVintageService;

    @EventListener
    public void handleTastingNoteUpdateEvent(WineVintageNoteEvent event) {
        wineVintageService.updateWineVintageNoteStatics(event.wineVintageId());
    }

}
