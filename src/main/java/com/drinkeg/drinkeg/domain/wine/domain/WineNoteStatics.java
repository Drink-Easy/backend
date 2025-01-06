package com.drinkeg.drinkeg.domain.wine.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineNoteStatics {
    private float avgSugarContent;
    private float avgAcidity;
    private float avgTannin;
    private float avgBody;
    private float avgAlcohol;
    private float avgMemberRating;

    private String nose1;
    private String nose2;
    private String nose3;

    @Builder
    public WineNoteStatics(float avgSugarContent, float avgAcidity, float avgTannin, float avgBody, float avgAlcohol, float avgMemberRating, String nose1, String nose2, String nose3) {
        this.avgSugarContent = avgSugarContent;
        this.avgAcidity = avgAcidity;
        this.avgTannin = avgTannin;
        this.avgBody = avgBody;
        this.avgAlcohol = avgAlcohol;
        this.avgMemberRating = avgMemberRating;
        this.nose1 = nose1;
        this.nose2 = nose2;
        this.nose3 = nose3;
    }
}
