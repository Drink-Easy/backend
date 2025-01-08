package com.drinkeg.drinkeg.domain.tastingNote.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TastingNoteWineSort {
    ALL("all"), RED("red"), WHITE("white"), SPARKLING("sparkling"), ROSE("rose"),ETCETERA("etc");
    private final String value;

    public static TastingNoteWineSort of(String value) {
        for (TastingNoteWineSort wineSort : TastingNoteWineSort.values()) {
            if (wineSort.getValue().equals(value)) {
                return wineSort;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 와인 종류입니다.");
    }
}
