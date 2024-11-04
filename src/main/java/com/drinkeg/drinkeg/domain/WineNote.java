package com.drinkeg.drinkeg.domain;

import com.drinkeg.drinkeg.converter.StringIntegerMapConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private float averageSugarContent;
    private float averageAcidity;
    private float averageTannin;
    private float averageBody;
    private float averageAlcohol;

    @Embedded
    private Nose nose;

    @Embedded
    private Palate palate;

    // 만족도 평균
    private float averageSatisfaction;

}
