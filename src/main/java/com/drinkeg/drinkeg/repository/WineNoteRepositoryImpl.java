package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Nose;
import com.drinkeg.drinkeg.domain.Palate;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.drinkeg.drinkeg.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.QWineNote.wineNote;

@Repository
@RequiredArgsConstructor
public class WineNoteRepositoryImpl implements WineNoteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // WineNote 업데이트 메서드
    public void updateWineNoteStatistics(Long wineId) {
        List<TastingNote> notes = queryFactory
                .selectFrom(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .fetch();

        if (notes.isEmpty()) return;

        // 평균 점수 계산
        float avgSugarContent = (float) notes.stream().mapToDouble(TastingNote::getSugarContent).average().orElse(0);
        float avgAcidity = (float) notes.stream().mapToDouble(TastingNote::getAcidity).average().orElse(0);
        float avgTannin = (float) notes.stream().mapToDouble(TastingNote::getTannin).average().orElse(0);
        float avgBody = (float) notes.stream().mapToDouble(TastingNote::getBody).average().orElse(0);
        float avgAlcohol = (float) notes.stream().mapToDouble(TastingNote::getAlcohol).average().orElse(0);
        float avgSatisfaction = (float) notes.stream().mapToDouble(TastingNote::getSatisfaction).average().orElse(0);

        // 빈도 상위 3개의 nose와 palate 가져오기
        Nose topNoses = getTop3Noses(notes.stream().flatMap(n -> n.getNose().stream()).collect(Collectors.toList()));
        Palate topPalates = getTop3Palates(notes.stream().flatMap(n -> n.getPalate().stream()).collect(Collectors.toList()));

        // Querydsl을 이용한 업데이트 쿼리
        queryFactory.update(wineNote)
                .where(wineNote.wine.id.eq(wineId))
                .set(wineNote.avgSugarContent, avgSugarContent)
                .set(wineNote.avgAcidity, avgAcidity)
                .set(wineNote.avgTannin, avgTannin)
                .set(wineNote.avgBody, avgBody)
                .set(wineNote.avgAlcohol, avgAlcohol)
                .set(wineNote.avgSatisfaction, avgSatisfaction)
                .set(wineNote.nose, topNoses)
                .set(wineNote.palate, topPalates)
                .execute();
    }

    private Nose getTop3Noses(List<String> elements) {
        Map<String, Long> frequencyMap = elements.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        List<String> topElements = frequencyMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        return new Nose(
                topElements.size() > 0 ? topElements.get(0) : null,
                topElements.size() > 1 ? topElements.get(1) : null,
                topElements.size() > 2 ? topElements.get(2) : null
        );
    }

    private Palate getTop3Palates(List<String> elements) {
        Map<String, Long> frequencyMap = elements.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        List<String> topElements = frequencyMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        return new Palate(
                topElements.size() > 0 ? topElements.get(0) : null,
                topElements.size() > 1 ? topElements.get(1) : null,
                topElements.size() > 2 ? topElements.get(2) : null
        );
    }
}
