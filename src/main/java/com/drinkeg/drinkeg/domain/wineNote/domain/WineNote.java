package com.drinkeg.drinkeg.domain.wineNote.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineNote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "wineNote", fetch = FetchType.LAZY)  // 연관 관계의 비주인
    private Wine wine;

    // 점수 평균
    private float avgSugarContent;
    private float avgAcidity;
    private float avgTannin;
    private float avgBody;
    private float avgAlcohol;

    // Getter 를 통해 Nose 를 얻을 수 있지만,
    // 그 내부 필드 조회를 위해선 Nose 에도 Getter 가 필요
    @Embedded
    private WineNoteNose wineNoteNose;

    // 만족도 평균
    private float avgMemberRating;

    @Builder
    public WineNote(Wine wine, float avgSugarContent, float avgAcidity, float avgTannin,
                    float avgBody, float avgAlcohol, WineNoteNose wineNoteNose, float avgMemberRating) {
        this.wine = wine;
        this.avgSugarContent = avgSugarContent;
        this.avgAcidity = avgAcidity;
        this.avgTannin = avgTannin;
        this.avgBody = avgBody;
        this.avgAlcohol = avgAlcohol;
        this.wineNoteNose = wineNoteNose;
        this.avgMemberRating = avgMemberRating;
    }

}
