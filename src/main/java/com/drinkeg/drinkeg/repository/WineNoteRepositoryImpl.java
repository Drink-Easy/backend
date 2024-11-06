package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Nose;
import com.drinkeg.drinkeg.domain.Palate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.drinkeg.drinkeg.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.QWineNote.wineNote;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineNoteRepositoryImpl implements WineNoteRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    // WineNote 업데이트 메서드
    public void updateWineNoteStatistics(Long wineId) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 평균 점수 한 번에 계산
        var result = queryFactory
                .select(
                        tastingNote.sugarContent.avg(),
                        tastingNote.acidity.avg(),
                        tastingNote.tannin.avg(),
                        tastingNote.body.avg(),
                        tastingNote.alcohol.avg(),
                        tastingNote.satisfaction.avg()
                )
                .from(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .fetchOne();

        // 결과가 null 인 경우 바로 반환
        if (result == null) return;

        // 평균 점수 추출 및 null 처리
        float avgSugarContent = Optional.ofNullable(result.get(0, Double.class)).orElse(0.0).floatValue();
        float avgAcidity = Optional.ofNullable(result.get(1, Double.class)).orElse(0.0).floatValue();
        float avgTannin = Optional.ofNullable(result.get(2, Double.class)).orElse(0.0).floatValue();
        float avgBody = Optional.ofNullable(result.get(3, Double.class)).orElse(0.0).floatValue();
        float avgAlcohol = Optional.ofNullable(result.get(4, Double.class)).orElse(0.0).floatValue();
        float avgSatisfaction = Optional.ofNullable(result.get(5, Double.class)).orElse(0.0).floatValue();

        // 상위 nose 요소 추출 (JSON_TABLE 사용)
// 상위 nose 요소 추출
        List<String> topNoses = entityManager.createQuery("""
        SELECT tn.noseElement 
        FROM TastingNoteNose tn
        WHERE tn.tastingNote.wine.id = :wineId
        GROUP BY tn.noseElement
        ORDER BY COUNT(tn.noseElement) DESC
        """, String.class)
                .setParameter("wineId", wineId)
                .setMaxResults(3)
                .getResultList();

        Nose nose = new Nose(
                !topNoses.isEmpty() ? topNoses.get(0) : null,
                topNoses.size() > 1 ? topNoses.get(1) : null,
                topNoses.size() > 2 ? topNoses.get(2) : null
        );

        // 상위 palate 요소 추출 (JSON_TABLE 사용)
        List<String> topPalates = entityManager.createQuery("""
        SELECT tp.palateElement 
        FROM TastingNotePalate tp
        WHERE tp.tastingNote.wine.id = :wineId
        GROUP BY tp.palateElement
        ORDER BY COUNT(tp.palateElement) DESC
        """, String.class)
                .setParameter("wineId", wineId)
                .setMaxResults(3)
                .getResultList();

        Palate palate = new Palate(
                !topPalates.isEmpty() ? topPalates.get(0) : null,
                topPalates.size() > 1 ? topPalates.get(1) : null,
                topPalates.size() > 2 ? topPalates.get(2) : null
        );

        // WineNote 업데이트
        queryFactory.update(wineNote)
                .where(wineNote.wine.id.eq(wineId))
                .set(wineNote.avgSugarContent, avgSugarContent)
                .set(wineNote.avgAcidity, avgAcidity)
                .set(wineNote.avgTannin, avgTannin)
                .set(wineNote.avgBody, avgBody)
                .set(wineNote.avgAlcohol, avgAlcohol)
                .set(wineNote.avgSatisfaction, avgSatisfaction)
                .set(wineNote.nose, nose)
                .set(wineNote.palate, palate)
                .execute();

        stopWatch.stop();
        System.out.println(stopWatch.getTime());
    }

}
