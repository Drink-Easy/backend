package com.drinkeg.drinkeg.domain.tastingNote.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public record WineNoteUpdateEvent(Long wineId) {
}
