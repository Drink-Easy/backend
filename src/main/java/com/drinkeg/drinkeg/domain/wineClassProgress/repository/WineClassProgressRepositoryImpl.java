package com.drinkeg.drinkeg.domain.wineClassProgress.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.drinkeg.drinkeg.wineClass.domain.QWineClass.wineClass;
import static com.drinkeg.drinkeg.wineLecture.domain.QWineLecture.wineLecture;
import static com.drinkeg.drinkeg.wineLecture.domain.QWineLectureComplete.wineLectureComplete;


@Repository
@RequiredArgsConstructor
@Transactional
public class WineClassProgressRepositoryImpl implements WineClassProgressRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Float> getProgress(Long wineClassId, Long memberId) {
        return Optional.ofNullable(queryFactory.select(
                        Expressions.cases()
                                .when(wineLecture.id.count().coalesce(0L).eq(0L))
                                .then(0.0f)
                                .otherwise(wineLectureComplete.id.count().floatValue()
                                        .divide(wineLecture.id.count().floatValue())
                                        .multiply(100.0f)))
                .from(wineClass)
                .join(wineLecture).on(wineLecture.wineClass.eq(wineClass))
                .leftJoin(wineLectureComplete)
                .on(wineLectureComplete.wineLecture.eq(wineLecture)
                        .and(wineLectureComplete.member.id.eq(memberId)))
                .where(wineClass.id.eq(wineClassId))
                .fetchOne());
    }
}
