package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNoteNose.tastingNoteNose;


@Repository
@RequiredArgsConstructor
@Transactional
public class TastingNoteNoseRepositoryImpl implements TastingNoteNoseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TastingNoteNose> getTastingNoteNoseListByUsername(String username) {
        return queryFactory.selectFrom(tastingNoteNose)
                .join(tastingNoteNose.tastingNote, tastingNote)
                .where(tastingNote.member.username.eq(username))
                .fetch();
    }
}
