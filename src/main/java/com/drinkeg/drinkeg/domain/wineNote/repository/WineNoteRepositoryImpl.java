
package com.drinkeg.drinkeg.domain.wineNote.repository;

import com.drinkeg.drinkeg.domain.wineNote.domain.WineNoteNose;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNoteNose.tastingNoteNose;
import static com.drinkeg.drinkeg.wineNote.domain.QWineNote.wineNote;


@Repository
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WineNoteRepositoryImpl implements WineNoteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // WineNote 업데이트 메서드
    public void updateWineNoteStatistics(Long wineId) {

        // 평균 점수 한 번에 계산 및 null 안전 처리
        Tuple result = queryFactory
                .select(
                        // 평균 값이 NULL 인 경우 0.0을 반환
                        tastingNote.sugarContent.avg().coalesce(0.0),
                        tastingNote.acidity.avg().coalesce(0.0),
                        tastingNote.tannin.avg().coalesce(0.0),
                        tastingNote.body.avg().coalesce(0.0),
                        tastingNote.alcohol.avg().coalesce(0.0),
                        tastingNote.rating.avg().coalesce(0.0)
                )
                .from(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .fetchOne();

        if (result == null) {
            log.info("No Tasting Notes found for wineId: {}", wineId);
            return;
        }

        float avgSugarContent = result.get(0, Double.class).floatValue();
        float avgAcidity = result.get(1, Double.class).floatValue();
        float avgTannin = result.get(2, Double.class).floatValue();
        float avgBody = result.get(3, Double.class).floatValue();
        float avgAlcohol = result.get(4, Double.class).floatValue();
        float avgMemberRating = result.get(5, Double.class).floatValue();

        // 상위 3개의 noseElement 추출
        List<String> topNoses = queryFactory
                .select(tastingNoteNose.noseElement)
                .from(tastingNoteNose)
                .where(tastingNoteNose.tastingNote.wine.id.eq(wineId))
                .groupBy(tastingNoteNose.noseElement)
                .orderBy(tastingNoteNose.noseElement.count().desc())
                .limit(3)
                .fetch();

        WineNoteNose wineNoteNose = new WineNoteNose(
                !topNoses.isEmpty() ? topNoses.get(0) : null,
                topNoses.size() > 1 ? topNoses.get(1) : null,
                topNoses.size() > 2 ? topNoses.get(2) : null
        );


        // WineNote 업데이트
        queryFactory.update(wineNote)
                .where(wineNote.wine.id.eq(wineId))
                .set(wineNote.avgSugarContent, avgSugarContent)
                .set(wineNote.avgAcidity, avgAcidity)
                .set(wineNote.avgTannin, avgTannin)
                .set(wineNote.avgBody, avgBody)
                .set(wineNote.avgAlcohol, avgAlcohol)
                .set(wineNote.avgMemberRating, avgMemberRating)
                .set(wineNote.wineNoteNose, wineNoteNose)
                .execute();
    }

}
