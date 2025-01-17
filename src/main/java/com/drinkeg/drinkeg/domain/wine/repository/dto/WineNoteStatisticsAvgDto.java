package com.drinkeg.drinkeg.domain.wine.repository.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WineNoteStatisticsAvgDto {
    private float avgSweetness;
    private float avgAcidity;
    private float avgTannin;
    private float avgBody;
    private float avgAlcohol;
    private float avgMemberRating;

    @Builder
    public WineNoteStatisticsAvgDto(float avgSweetness, float avgAcidity, float avgTannin, float avgBody, float avgAlcohol, float avgMemberRating) {
        this.avgSweetness = avgSweetness;
        this.avgAcidity = avgAcidity;
        this.avgTannin = avgTannin;
        this.avgBody = avgBody;
        this.avgAlcohol = avgAlcohol;
        this.avgMemberRating = avgMemberRating;
    }
}
