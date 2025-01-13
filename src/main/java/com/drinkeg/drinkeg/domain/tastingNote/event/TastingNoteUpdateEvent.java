package com.drinkeg.drinkeg.domain.tastingNote.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TastingNoteUpdateEvent {
    private final Long wineId;
}
