package com.drinkeg.drinkeg.tastingNote.dao;

import com.drinkeg.drinkeg.tastingNote.domain.TastingNote;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.drinkeg.drinkeg.domain.QMember.member;
import static com.drinkeg.drinkeg.domain.QWine.wine;
import static com.drinkeg.drinkeg.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.tastingNote.domain.QTastingNoteNose.tastingNoteNose;

@Repository
@RequiredArgsConstructor
@Transactional
public class TastingNoteRepositoryImpl implements TastingNoteRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TastingNote> findTastingNoteWithWineAndNoseById(Long tastingNoteId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(tastingNote)
                        .leftJoin(tastingNote.wine, wine).fetchJoin()
                        .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                        .where(tastingNote.id.eq(tastingNoteId))
                        .fetchOne()
        );
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
    public List<TastingNote> findTastingNotesWithWineAndNoseByUsername(String username) {
        return queryFactory.selectFrom(tastingNote)
                .leftJoin(tastingNote.member, member).fetchJoin()
                .leftJoin(tastingNote.wine, wine).fetchJoin()
                .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                .where(member.username.eq(username))
                .fetch();
    }
}
