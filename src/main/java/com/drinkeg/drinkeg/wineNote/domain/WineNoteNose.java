package com.drinkeg.drinkeg.wineNote.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class WineNoteNose {

    private String nose1;
    private String nose2;
    private String nose3;

    public WineNoteNose(String nose1, String nose2, String nose3) {
        this.nose1 = nose1;
        this.nose2 = nose2;
        this.nose3 = nose3;
    }

    public WineNoteNose() {} // 기본 생성자
}
