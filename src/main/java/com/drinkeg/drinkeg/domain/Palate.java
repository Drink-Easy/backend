package com.drinkeg.drinkeg.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Palate {

    private String palate1;
    private String palate2;
    private String palate3;

    public Palate(String palate1, String palate2, String palate3) {
        this.palate1 = palate1;
        this.palate2 = palate2;
        this.palate3 = palate3;
    }

    public Palate() {} // 기본 생성자
}