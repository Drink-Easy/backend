package com.drinkeg.drinkeg.tastingNote.repository;

import com.drinkeg.drinkeg.tastingNote.domain.TastingNoteNose;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.drinkeg.drinkeg.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.tastingNote.domain.QTastingNoteNose.tastingNoteNose;

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
