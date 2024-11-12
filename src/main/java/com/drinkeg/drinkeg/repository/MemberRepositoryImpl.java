package com.drinkeg.drinkeg.repository;


import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.QMember;
import com.drinkeg.drinkeg.domain.QTastingNote;
import com.drinkeg.drinkeg.domain.QWine;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Member> findMemberWithTastingNoteByUsername(String username) {
        return Optional.ofNullable(
                queryFactory.selectFrom(QMember.member)
                        .leftJoin(QMember.member.tastingNotes, QTastingNote.tastingNote).fetchJoin()
                        // .leftJoin(QTastingNote.tastingNote.wine, QWine.wine).fetchJoin()
                        .where(QMember.member.username.eq(username))
                        .fetchOne()
        );
    }
}
