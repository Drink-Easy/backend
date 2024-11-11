package com.drinkeg.drinkeg.service.wineNoteService;

import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.WineNote;

public interface WineNoteService {

    public void updateWineNote(WineNote wineNote, TastingNote t, boolean add);

}
