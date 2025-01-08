package com.drinkeg.drinkeg.domain.tastingNote.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TastingNoteWineSort {
    ALL("전체"), RED("레드"), WHITE("화이트"), SPARKLING("스파클링"), ROSE("로제"), ETCETERA("기타");
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
