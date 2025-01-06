package com.drinkeg.drinkeg.domain.wineNote.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WineNoteUpdateEvent {
    private final Long wineId;
}
