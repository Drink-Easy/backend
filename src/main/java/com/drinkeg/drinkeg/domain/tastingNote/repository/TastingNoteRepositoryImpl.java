package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.QTastingNoteSortCountResponse;
import com.drinkeg.drinkeg.domain.wine.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.dto.WineNoteStatisticsAvgDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNoteNose.tastingNoteNose;
import static com.drinkeg.drinkeg.domain.wine.domain.QWine.wine;
import static com.drinkeg.drinkeg.domain.wine.wineVintage.domain.QWineVintage.wineVintage;

@Repository
@RequiredArgsConstructor
@Transactional
public class TastingNoteRepositoryImpl implements TastingNoteRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TastingNote> findAllTastingNoteBy(Long wineId, SortType sort, Pageable pageable) {
        return queryFactory.selectFrom(tastingNote)
                .where(tastingNote.wineVintage.wine.id.eq(wineId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderCondition(sort), tastingNote.id.desc())
                .fetch();
    }

    @Override
    public long countTastingNoteByWineId(Long wineId) {
        return queryFactory
                .select(tastingNote.count())
                .from(tastingNote)
                .where(tastingNote.wineVintage.wine.id.eq(wineId))
                .fetchOne();
    }

    @Override
    public WineNoteStatisticsAvgDto findWineStatisticsByWineId(Long wineId) {
        return queryFactory
                .select(Projections.fields(WineNoteStatisticsAvgDto.class,
                        tastingNote.sweetness.avg().coalesce(0.0).floatValue().as("avgSweetness"),
                        tastingNote.acidity.avg().coalesce(0.0).floatValue().as("avgAcidity"),
                        tastingNote.tannin.avg().coalesce(0.0).floatValue().as("avgTannin"),
                        tastingNote.body.avg().coalesce(0.0).floatValue().as("avgBody"),
                        tastingNote.alcohol.avg().coalesce(0.0).floatValue().as("avgAlcohol"),
                        tastingNote.rating.avg().coalesce(0.0).floatValue().as("avgMemberRating")
                ))
                .from(tastingNote)
                .where(tastingNote.wineVintage.wine.id.eq(wineId))
                .fetchOne();
    }

    @Override
    public WineNoteStatisticsAvgDto findWineVintageStatisticsByWineVintageId(Long wineVintageId){
        return queryFactory
                .select(Projections.fields(WineNoteStatisticsAvgDto.class,
                        tastingNote.sweetness.avg().coalesce(0.0).floatValue().as("avgSweetness"),
                        tastingNote.acidity.avg().coalesce(0.0).floatValue().as("avgAcidity"),
                        tastingNote.tannin.avg().coalesce(0.0).floatValue().as("avgTannin"),
                        tastingNote.body.avg().coalesce(0.0).floatValue().as("avgBody"),
                        tastingNote.alcohol.avg().coalesce(0.0).floatValue().as("avgAlcohol"),
                        tastingNote.rating.avg().coalesce(0.0).floatValue().as("avgMemberRating")
                ))
                .from(tastingNote)
                .where(tastingNote.wineVintage.id.eq(wineVintageId))
                .fetchOne();

    }

    @Override
    public List<String> findTopThreeNoseByWineId(Long wineId) {
        return queryFactory
                .select(tastingNoteNose.noseElement)
                .from(tastingNoteNose)
                .join(tastingNoteNose.tastingNote, tastingNote)
                .join(tastingNote.wineVintage, wineVintage)
                .join(wineVintage.wine, wine)
                .where(wine.id.eq(wineId)) // 안전하게 접근 가능
                .groupBy(tastingNoteNose.noseElement)
                .orderBy(
                        tastingNoteNose.noseElement.count().desc(),
                        tastingNoteNose.noseElement.asc()
                )
                .limit(3)
                .fetch();
    }

    @Override
    public List<String> findTopThreeNoseByWineVintageId(Long wineVintageId) {
        return queryFactory
                .select(tastingNoteNose.noseElement)
                .from(tastingNoteNose)
                .where(tastingNoteNose.tastingNote.wineVintage.id.eq(wineVintageId))
                .groupBy(tastingNoteNose.noseElement)
                .orderBy(tastingNoteNose.noseElement.count().desc())
                .orderBy(tastingNoteNose.noseElement.asc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<TastingNote> findTastingNoteBySortAndUsername(TastingNoteWineSort wineSort, String username, Pageable pageable) {
        return queryFactory
                .selectFrom(tastingNote)
                .leftJoin(tastingNote.wineVintage.wine, wine).fetchJoin()
                .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                .where(
                        tastingNote.member.username.eq(username),
                        wineSortIn(wineSort)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(tastingNote.id.desc())
                .fetch();
    }

    @Override
    public long countTastingNoteBySortAndUsername(TastingNoteWineSort wineSort, String username) {
        return queryFactory
                .select(tastingNote.count())
                .from(tastingNote)
                .leftJoin(tastingNote.wineVintage.wine, wine)
                .where(
                        tastingNote.member.username.eq(username),
                        wineSortIn(wineSort)
                )
                .fetchOne();
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

    @Override
    public List<TastingNote> searchTastingNoteByWineName(String searchName, String username, Pageable pageable) {
        return queryFactory
                .selectFrom(tastingNote)
                .leftJoin(tastingNote.wineVintage.wine, wine).fetchJoin()
                .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                .where(
                        tastingNote.member.username.eq(username),
                        tastingNote.wineVintage.wine.searchName.contains(searchName)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(tastingNote.id.desc())
                .fetch();
    }

    @Override
    public long countSearchTastingNoteByWineName(String searchName, String username) {
        return queryFactory
                .select(tastingNote.count())
                .from(tastingNote)
                .leftJoin(tastingNote.wineVintage.wine, wine)
                .where(
                        tastingNote.member.username.eq(username),
                        tastingNote.wineVintage.wine.searchName.contains(searchName)
                )
                .fetchOne();
    }


    private OrderSpecifier<?> orderCondition(SortType sortType) {
        return switch (sortType) {
            case LATEST -> new OrderSpecifier<>(Order.DESC, tastingNote.updatedAt);
            case OLDEST -> new OrderSpecifier<>(Order.ASC, tastingNote.updatedAt);
            case HIGH_RATING -> new OrderSpecifier<>(Order.DESC, tastingNote.rating);
            case LOW_RATING -> new OrderSpecifier<>(Order.ASC, tastingNote.rating);
        };
    }

    private BooleanExpression wineSortIn(TastingNoteWineSort wineSort) {
        if (wineSort.equals(TastingNoteWineSort.ALL)) return null;
        else if (wineSort.equals(TastingNoteWineSort.ETCETERA)) return wine.sort.in("주정강화", "기타");
        else return wine.sort.eq(wineSort.getValue());
    }

    private NumberExpression<Integer> getWineCount(String wineSort) {
        Predicate condition = wineSort
                .equals("기타") ? tastingNote.wineVintage.wine.sort.in("주정강화", "기타")
                : tastingNote.wineVintage.wine.sort.eq(wineSort);

        return new CaseBuilder()
                .when(condition).then(1).otherwise(0)
                .sum();
    }

}