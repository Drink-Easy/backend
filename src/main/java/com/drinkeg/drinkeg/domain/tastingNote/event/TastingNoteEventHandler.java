package com.drinkeg.drinkeg.domain.tastingNote.event;

import com.drinkeg.drinkeg.domain.tastingNote.service.TastingNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Component
public class TastingNoteEventHandler {
    private final TastingNoteService tastingNoteService;

    @EventListener
    public void handleWineNoteEvent(RemoveTastingNoteMemberEvent event) {
        tastingNoteService.setTastingNoteMemberNull(event.getUsername());
    }

}
