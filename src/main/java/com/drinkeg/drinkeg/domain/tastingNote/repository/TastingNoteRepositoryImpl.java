package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.QTastingNoteSortCountResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
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
    public Optional<TastingNote> findTastingNoteWithWineAndNoseAndMemberById(Long noteId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(tastingNote)
                        .leftJoin(tastingNote.wine, wine).fetchJoin()
                        .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                        .leftJoin(tastingNote.member).fetchJoin()
                        .where(tastingNote.id.eq(noteId))
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
    public TastingNoteSortCountResponse findTastingNoteSortCountsByUsername(String username) {
        return queryFactory
                .select(
                        new QTastingNoteSortCountResponse(
                                tastingNote.count().castToNum(Integer.class).as("totalCount"),
                                getWineCount("레드"),
                                getWineCount("화이트"),
                                getWineCount("스파클링"),
                                getWineCount("로제"),
                                new CaseBuilder()
                                        .when(tastingNote.wine.sort.in("주정강화", "기타")).then(1).otherwise(0)
                                        .sum().as("etcCount")
                        )
                )
                .from(tastingNote)
                .where(tastingNote.member.username.eq(username))
                .fetchOne();
    }

    private BooleanExpression wineSortIn(TastingNoteWineSort wineSort) {
        if (wineSort.equals(TastingNoteWineSort.ALL)) return null;
        else if (wineSort.equals(TastingNoteWineSort.ETCETERA)) return wine.sort.in("주정강화", "기타");
        else return wine.sort.eq(convertSort(wineSort));
    }

    private String convertSort(TastingNoteWineSort wineSort) {
        return switch (wineSort) {
            case RED -> "레드";
            case WHITE -> "화이트";
            case SPARKLING -> "스파클링";
            case ROSE -> "로제";
            default -> throw new GeneralException(ErrorStatus.SORT_NOT_FOUND);
        };
    }

    // 와인 종류에 따른 개수 반환
    private NumberExpression<Integer> getWineCount(String wineSort) {
        return new CaseBuilder()
                .when(tastingNote.wine.sort.eq(wineSort)).then(1).otherwise(0)
                .sum();
    }

}