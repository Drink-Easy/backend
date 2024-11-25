package com.drinkeg.drinkeg.repository.wineClassProgress;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.drinkeg.drinkeg.domain.QWineClass.wineClass;
import static com.drinkeg.drinkeg.domain.QWineLecture.wineLecture;
import static com.drinkeg.drinkeg.domain.QWineLectureComplete.wineLectureComplete;


@Repository
@RequiredArgsConstructor
@Transactional
public class WineClassProgressRepositoryImpl implements WineClassProgressRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public float getProgress(Long wineClassId, Long memberId) {
        return queryFactory.select(Expressions.cases()
                                .when(
                                        wineLecture.id.count().isNull()
                                        .or(wineLectureComplete.id.count().isNull())
                                        .or(wineLecture.id.count().eq(0L))
                                        .or(wineLectureComplete.id.count().eq(0L))
                                )
                                .then(0.0f)
                                .otherwise(
                                        wineLectureComplete.id.count().floatValue()
                                        .divide(wineLecture.id.count().floatValue())
                                        .multiply(100.0f))
                                )
                .from(wineClass)
                .leftJoin(wineLecture)
                .on(wineLecture.wineClass.eq(wineClass))
                .leftJoin(wineLectureComplete)
                .on(wineLectureComplete.wineLecture.eq(wineLecture).and(wineLectureComplete.member.id.eq(memberId)))
                .where(wineClass.id.eq(wineClassId))
                .fetchOne();

    }
}
