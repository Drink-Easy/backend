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

    // 점수 합
    private float totalSugarContent;
    private float totalAcidity;
    private float totalTannin;
    private float totalBody;
    private float totalAlcohol;

    // 와인에 대한 테이스팅 노트 개수
    private int tastingNoteCount;

    // 향과 맛 빈도를 저장할 Map
    @Convert(converter = StringIntegerMapConverter.class)  // Map 컨버터 필요
    private Map<String, Integer> noseFrequency = new HashMap<>();

    @Convert(converter = StringIntegerMapConverter.class)  // Map 컨버터 필요
    private Map<String, Integer> palateFrequency = new HashMap<>();

    private float totalSatisfaction;

    // 새로운 테이스팅 점수, 별점, 향/맛 추가 메서드
    public void addTastingNoteScores(TastingNote tastingNote) {
        this.totalSugarContent += tastingNote.getSugarContent();
        this.totalAcidity += tastingNote.getAcidity();
        this.totalTannin += tastingNote.getTannin();
        this.totalBody += tastingNote.getBody();
        this.totalAlcohol += tastingNote.getAlcohol();
        this.totalSatisfaction += tastingNote.getSatisfaction();

        // 맛/향 빈도 증가
        updateNoseFrequency(tastingNote.getNose(), 1);
        updatePalateFrequency(tastingNote.getPalate(), 1);

        tastingNoteCount++;
    }

    // 테이스팅 점수, 별점, 향/맛 삭제 메서드
    public void removeTastingNoteScores(TastingNote tastingNote) {
        // 점수 필드 감소
        totalSugarContent -= tastingNote.getSugarContent();
        totalAcidity -= tastingNote.getAcidity();
        totalTannin -= tastingNote.getTannin();
        totalBody -= tastingNote.getBody();
        totalAlcohol -= tastingNote.getAlcohol();
        totalSatisfaction -= tastingNote.getSatisfaction();

        // 맛/향 빈도 감소
        updateNoseFrequency(tastingNote.getNose(), -1);
        updatePalateFrequency(tastingNote.getPalate(), -1);

        tastingNoteCount--;
    }

    // 맛 업데이트
    public void updateSugarContent(float sugarContent) {
        this.totalSugarContent += sugarContent;
    }
    public void updateAcidity(float acidity) {
        this.totalAcidity += acidity;
    }
    public void updateTannin(float tannin) {
        this.totalTannin += tannin;
    }
    public void updateBody(float body) {
        this.totalBody += body;
    }
    public void updateAlcohol(float alcohol) {
        this.totalAlcohol += alcohol;
    }

    // 향과 맛 빈도 업데이트 메서드
    public void updateNoseFrequency(List<String> noses, int add) {
        for (String nose : noses) {
            int updatedFrequency = noseFrequency.getOrDefault(nose, 0) + add;

            if (updatedFrequency == 0) {
                noseFrequency.remove(nose);  // 값이 0이면 항목 삭제
            } else {
                noseFrequency.put(nose, updatedFrequency);
            }
        }
    }
    public void updatePalateFrequency(List<String> palates, int add) {
        for (String palate : palates) {
            int updatedFrequency = palateFrequency.getOrDefault(palate, 0) + add;

            if (updatedFrequency == 0) {
                palateFrequency.remove(palate);  // 값이 0이면 항목 삭제
            } else {
                palateFrequency.put(palate, updatedFrequency);
            }
        }
    }

    // 사용자 별점 업데이트
    public void updateSatisfaction(float satisfaction){
        this.totalSatisfaction += satisfaction;
    }

    // 각 점수의 평균 반환 메서드
    public float getSugarContent() {
        return tastingNoteCount > 0 ? totalSugarContent / tastingNoteCount : 0;
    }

    public float getAcidity() {
        return tastingNoteCount > 0 ? totalAcidity / tastingNoteCount : 0;
    }

    public float getTannin() {
        return tastingNoteCount > 0 ? totalTannin / tastingNoteCount : 0;
    }

    public float getBody() {
        return tastingNoteCount > 0 ? totalBody / tastingNoteCount : 0;
    }

    public float getAlcohol() {
        return tastingNoteCount > 0 ? totalAlcohol / tastingNoteCount : 0;
    }

    public float getSatisfaction() {
        return tastingNoteCount > 0 ? totalSatisfaction / tastingNoteCount : 0;
    }

    // Top 3 업데이트 메서드
    public List<String> getTop3Nose() {
        return noseFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<String> getTop3Palate() {
        return palateFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
