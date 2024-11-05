package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Nose;
import com.drinkeg.drinkeg.domain.Palate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        // 평균 점수 결과 추출 후 float 로 변환
        Double sugarContent = result.get(0, Double.class);
        float avgSugarContent = (sugarContent != null) ? sugarContent.floatValue() : 0.0f;

        Double acidity = result.get(1, Double.class);
        float avgAcidity = (acidity != null) ? acidity.floatValue() : 0.0f;

        Double tannin = result.get(2, Double.class);
        float avgTannin = (tannin != null) ? tannin.floatValue() : 0.0f;

        Double body = result.get(3, Double.class);
        float avgBody = (body != null) ? body.floatValue() : 0.0f;

        Double alcohol = result.get(4, Double.class);
        float avgAlcohol = (alcohol != null) ? alcohol.floatValue() : 0.0f;

        Double satisfaction = result.get(5, Double.class);
        float avgSatisfaction = (satisfaction != null) ? satisfaction.floatValue() : 0.0f;

        // 상위 nose 요소 추출 (Native Query 사용)
        List<String> topNoses = entityManager.createNativeQuery("""
            SELECT nose_element FROM (
                SELECT JSON_UNQUOTE(JSON_EXTRACT(nose, CONCAT('$[', numbers.n, ']'))) AS nose_element
                FROM tasting_note,
                    (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS numbers
                WHERE wine_id = :wineId
                AND JSON_UNQUOTE(JSON_EXTRACT(nose, CONCAT('$[', numbers.n, ']'))) IS NOT NULL
            ) AS nose_elements
            GROUP BY nose_element
            ORDER BY COUNT(*) DESC
            LIMIT 3
            """)
                .setParameter("wineId", wineId)
                .getResultList();

        Nose nose = new Nose(
                !topNoses.isEmpty() ? topNoses.get(0) : null,
                topNoses.size() > 1 ? topNoses.get(1) : null,
                topNoses.size() > 2 ? topNoses.get(2) : null
        );

        // 상위 palate 요소 추출 (Native Query 사용)
        List<String> topPalates = entityManager.createNativeQuery("""
            SELECT palate_element FROM (
                SELECT JSON_UNQUOTE(JSON_EXTRACT(palate, CONCAT('$[', numbers.n, ']'))) AS palate_element
                FROM tasting_note,
                    (SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
                    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) AS numbers
                WHERE wine_id = :wineId
                AND JSON_UNQUOTE(JSON_EXTRACT(palate, CONCAT('$[', numbers.n, ']'))) IS NOT NULL
            ) AS palate_elements
            GROUP BY palate_element
            ORDER BY COUNT(*) DESC
            LIMIT 3
            """)
                .setParameter("wineId", wineId)
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
    }
}
