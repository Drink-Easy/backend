package com.drinkeg.drinkeg.domain.wine.domain;

import com.drinkeg.drinkeg.domain.wine.repository.dto.WineNoteStatisticsAvgDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineNoteStatistics {
    private float avgSweetness;
    private float avgAcidity;
    private float avgTannin;
    private float avgBody;
    private float avgAlcohol;
    private float avgMemberRating;

    private String nose1;
    private String nose2;
    private String nose3;

    public WineNoteStatistics updateAvgStatistics(WineNoteStatisticsAvgDto wineNoteStatisticsAvgDto) {
        this.avgSweetness = wineNoteStatisticsAvgDto.getAvgSweetness();
        this.avgAcidity = wineNoteStatisticsAvgDto.getAvgAcidity();
        this.avgTannin = wineNoteStatisticsAvgDto.getAvgTannin();
        this.avgBody = wineNoteStatisticsAvgDto.getAvgBody();
        this.avgAlcohol = wineNoteStatisticsAvgDto.getAvgAlcohol();
        this.avgMemberRating = wineNoteStatisticsAvgDto.getAvgMemberRating();
        return this;
    }

    public WineNoteStatistics updateNose(List<String> noseList) {
        nose1 = !noseList.isEmpty() ? noseList.get(0) : null;
        nose2 = noseList.size() > 1 ? noseList.get(1) : null;
        nose3 = noseList.size() > 2 ? noseList.get(2) : null;
        return this;
    }

    public static WineNoteStatistics create(float avgSweetness, float avgAcidity, float avgTannin,
                                            float avgBody, float avgAlcohol, float avgMemberRating,
                                            String nose1, String nose2, String nose3) {
        return WineNoteStatistics.builder()
                .avgSweetness(avgSweetness)
                .avgAcidity(avgAcidity)
                .avgTannin(avgTannin)
                .avgBody(avgBody)
                .avgAlcohol(avgAlcohol)
                .avgMemberRating(avgMemberRating)
                .nose1(nose1)
                .nose2(nose2)
                .nose3(nose3).build();
    }

    public static WineNoteStatistics create() {
        return create(0, 0, 0, 0, 0, 0,
                null, null, null);
    }

    @Builder
    public WineNoteStatistics(float avgSweetness, float avgAcidity, float avgTannin,
                              float avgBody, float avgAlcohol, float avgMemberRating,
                              String nose1, String nose2, String nose3) {
        this.avgSweetness = avgSweetness;
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
