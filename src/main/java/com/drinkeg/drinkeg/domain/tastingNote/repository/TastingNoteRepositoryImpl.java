package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNoteNose;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.domain.QWine;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    public List<TastingNote> findTastingNoteBySortAndUsername(String sort, String username) {
        return queryFactory
                .selectFrom(tastingNote)
                .leftJoin(tastingNote.wine, wine).fetchJoin()
                .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                .where(
                        tastingNote.member.username.eq(username),
                        wineSortIn(sort)
                )
                .fetch();
    }

    private BooleanExpression wineSortIn(String sort) {
        if (sort.equals("all")) return null;
        else if (sort.equals("etc")) return wine.sort.in("주정강화", "기타");
        else return wine.sort.eq(convertSort(sort));
    }

    private String convertSort(String sort) {
        return switch (sort) {
            case "red" -> "레드";
            case "white" -> "화이트";
            case "sparkling" -> "스파클링";
            case "rose" -> "로제";
            default -> throw new GeneralException(ErrorStatus.SORT_NOT_FOUND);
        };
    }

}