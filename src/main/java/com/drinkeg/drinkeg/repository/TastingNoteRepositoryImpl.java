package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.QMember;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.drinkeg.drinkeg.domain.QMember.member;
import static com.drinkeg.drinkeg.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.QTastingNoteNose.tastingNoteNose;
import static com.drinkeg.drinkeg.domain.QWine.wine;

@Repository
@RequiredArgsConstructor
@Transactional
public class TastingNoteRepositoryImpl implements TastingNoteRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TastingNote> findByIdWithWineAndNose(Long tastingNoteId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(tastingNote)
                        .leftJoin(tastingNote.wine, wine).fetchJoin()
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
