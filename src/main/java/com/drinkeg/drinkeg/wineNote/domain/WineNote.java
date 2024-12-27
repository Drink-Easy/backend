package com.drinkeg.drinkeg.wineNote.domain;

import com.drinkeg.drinkeg.domain.BaseEntity;
import com.drinkeg.drinkeg.wine.domain.Wine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

}
