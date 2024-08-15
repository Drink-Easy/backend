package com.drinkeg.drinkeg.domain;

import com.drinkeg.drinkeg.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TastingNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wine_id")
    private Wine wine;

    private String color;

    // 점수 0 ~ 5
    private int sugarContent;
    private int acidity;
    private int tannin;
    private int body;
    private int alcohol;

    // 향 여러개를 ", "로 구분해서 List로 저장.
    @Convert(converter = StringListConverter.class)
    private List<String> scentAroma = new ArrayList<>();

    @Convert(converter = StringListConverter.class)
    private List<String> scentTaste = new ArrayList<>();

    @Convert(converter = StringListConverter.class)
    private List<String> scentFinish = new ArrayList<>();

    // 만족도 0 ~ 5, 소수점 가능
    private float satisfaction;

    private String review;


    // 와인 업데이트
    public void updateWine(Wine wine) {
        this.wine = wine;
    }
    // 색상 업데이트
    public void updateColor(String color) {
        this.color = color;
    }

    // 맛 업데이트
    public void updateSugarContent(int sugarContent) {
        this.sugarContent = sugarContent;
    }
    public void updateAcidity(int acidity) {
        this.acidity = acidity;
    }
    public void updateTannin(int tannin) {
        this.tannin = tannin;
    }
    public void updateBody(int body) {
        this.body = body;
    }
    public void updateAlcohol(int alcohol) {
        this.alcohol = alcohol;
    }

    // 향 업데이트
    public void updateScentAroma(List<String> scentAroma) {
        this.scentAroma = scentAroma;
    }
    public void updateScentTaste(List<String> scentTaste) {
        this.scentTaste = scentTaste;
    }
    public void updateScentFinish(List<String> scentFinish) {
        this.scentFinish = scentFinish;
    }

    // 만족도 업데이트
    public void updateSatisfaction(float satisfaction) {
        this.satisfaction = satisfaction;
    }
    // 메모 업데이트
    public void updateMemo(String review) {
        this.review = review;
    }



}
