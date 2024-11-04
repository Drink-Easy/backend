package com.drinkeg.drinkeg.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class Nose {

    private String nose1;
    private String nose2;
    private String nose3;

    public Nose(String nose1, String nose2, String nose3) {
        this.nose1 = nose1;
        this.nose2 = nose2;
        this.nose3 = nose3;
    }

    public Nose() {} // 기본 생성자
}
