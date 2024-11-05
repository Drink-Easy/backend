package com.drinkeg.drinkeg.service.wineNoteService;

import com.drinkeg.drinkeg.event.WineNoteUpdateEvent;

public interface WineNoteService {

    public void handleWineNoteEvent(WineNoteUpdateEvent event);

}
