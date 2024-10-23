package com.drinkeg.drinkeg.domain;

import com.drinkeg.drinkeg.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    // 점수 평균 0 ~ 5
    private float sugarContent;
    private float acidity;
    private float tannin;
    private float body;
    private float alcohol;

    // 향 여러개를 ", "로 구분해서 List 로 저장.
    @Builder.Default
    @Convert(converter = StringListConverter.class)
    private final List<String> nose = new ArrayList<>();

    @Builder.Default
    @Convert(converter = StringListConverter.class)
    private final List<String> palate = new ArrayList<>();

    private float rating;

    // 맛 업데이트
    public void updateSugarContent(float sugarContent) {
        this.sugarContent = sugarContent;
    }
    public void updateAcidity(float acidity) {
        this.acidity = acidity;
    }
    public void updateTannin(float tannin) {
        this.tannin = tannin;
    }
    public void updateBody(float body) {
        this.body = body;
    }
    public void updateAlcohol(float alcohol) {
        this.alcohol = alcohol;
    }

    // 향 업데이트 : 기존 내용을 지우고, 새 내용으로 덮어 쓰는 방식
    public void updateNose(List<String> nose) {
        this.nose.clear();
        this.nose.addAll(nose);
    }
    public void updatePalete(List<String> palate) {
        this.palate.clear();
        this.palate.addAll(palate);
    }

    // 사용자 별점 업데이트
    public void updateRating(float rating){
        this.rating = rating;
    }

}
