package com.drinkeg.drinkeg.domain;

import com.drinkeg.drinkeg.converter.StringIntegerMapConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineNote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "wine_id")
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
    private Nose nose;

    // 만족도 평균
    private float avgSatisfaction;

}
