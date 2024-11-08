package com.drinkeg.drinkeg.event.wineNoteEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WineNoteUpdateEventDTO {
    private final Long wineId;
}
