package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.QTastingNoteSortCountResponse;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.repository.dto.WineNoteStatisticsAvgDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNoteNose.tastingNoteNose;
import static com.drinkeg.drinkeg.domain.wine.domain.QWine.wine;

@Repository
@RequiredArgsConstructor
@Transactional
public class TastingNoteRepositoryImpl implements TastingNoteRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public WineNoteStatisticsAvgDto findWineNoteStatisticsByWineId(Long wineId) {
        return queryFactory
                .select(Projections.fields(WineNoteStatisticsAvgDto.class,
                        tastingNote.sugarContent.avg().coalesce(0.0).floatValue().as("avgSugarContent"),
                        tastingNote.acidity.avg().coalesce(0.0).floatValue().as("avgAcidity"),
                        tastingNote.tannin.avg().coalesce(0.0).floatValue().as("avgTannin"),
                        tastingNote.body.avg().coalesce(0.0).floatValue().as("avgBody"),
                        tastingNote.alcohol.avg().coalesce(0.0).floatValue().as("avgAlcohol"),
                        tastingNote.rating.avg().coalesce(0.0).floatValue().as("avgMemberRating")
                ))
                .from(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .fetchOne();
    }

    @Override
    public List<String> findTopThreeNoseByWineId(Long wineId) {
        return queryFactory
                .select(tastingNoteNose.noseElement)
                .from(tastingNoteNose)
                .where(tastingNoteNose.tastingNote.wine.id.eq(wineId))
                .groupBy(tastingNoteNose.noseElement)
                .orderBy(tastingNoteNose.noseElement.count().desc())
                .orderBy(tastingNoteNose.noseElement.asc())
                .limit(3)
                .fetch();
    }

    @Override
    public Optional<TastingNote> findTastingNoteWithNoseById(Long tastingNoteId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(tastingNote)
                        .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                        .where(tastingNote.id.eq(tastingNoteId))
                        .fetchOne()
        );
    }

    @Override
    public List<TastingNote> findTastingNoteBySortAndUsername(TastingNoteWineSort wineSort, String username) {
        return queryFactory
                .selectFrom(tastingNote)
                .leftJoin(tastingNote.wine, wine).fetchJoin()
                .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                .where(
                        tastingNote.member.username.eq(username),
                        wineSortIn(wineSort)
                )
                .orderBy(tastingNote.id.desc())
                .fetch();
    }


    @Override
    public List<TastingNote> findAllTastingNoteBy(Long wineId, SortType sort) {
        return queryFactory.selectFrom(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .orderBy(orderCondition(sort), tastingNote.id.desc())
                .fetch();
    }

    private OrderSpecifier<?> orderCondition(SortType sortType) {
        return switch (sortType) {
            case LATEST -> new OrderSpecifier<>(Order.DESC, tastingNote.updatedAt);
            case OLDEST -> new OrderSpecifier<>(Order.ASC, tastingNote.updatedAt);
            case HIGH_RATING -> new OrderSpecifier<>(Order.DESC, tastingNote.rating);
            case LOW_RATING -> new OrderSpecifier<>(Order.ASC, tastingNote.rating);
        };
    }

    @Override
    public TastingNoteSortCountResponse findTastingNoteSortCountsByUsername(String username) {
        return queryFactory
                .select(
                        new QTastingNoteSortCountResponse(
                                tastingNote.count().castToNum(Integer.class).as("totalCount"),
                                getWineCount("레드"),
                                getWineCount("화이트"),
                                getWineCount("스파클링"),
                                getWineCount("로제"),
                                getWineCount("기타")
                        )
                )
                .from(tastingNote)
                .where(tastingNote.member.username.eq(username))
                .fetchOne();
    }

    private BooleanExpression wineSortIn(TastingNoteWineSort wineSort) {
        if (wineSort.equals(TastingNoteWineSort.ALL)) return null;
        else if (wineSort.equals(TastingNoteWineSort.ETCETERA)) return wine.sort.in("주정강화", "기타");
        else return wine.sort.eq(wineSort.getValue());
    }


    // 와인 종류에 따른 개수 반환
    private NumberExpression<Integer> getWineCount(String wineSort) {
        Predicate condition = wineSort.equals("기타")
                ? tastingNote.wine.sort.in("주정강화", "기타")
                : tastingNote.wine.sort.eq(wineSort);

        return new CaseBuilder()
                .when(condition).then(1).otherwise(0)
                .sum();
    }

}