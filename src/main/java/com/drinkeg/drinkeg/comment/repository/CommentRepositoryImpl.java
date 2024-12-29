package com.drinkeg.drinkeg.comment.repository;

import com.drinkeg.drinkeg.comment.domain.QComment;
import com.drinkeg.drinkeg.recomment.domain.QRecomment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countCommentsAndRecommentsByPartyId(Long partyId) {
        QComment comment = QComment.comment;
        QRecomment recomment = QRecomment.recomment;

        // 댓글 수 카운트 (삭제되지 않은 댓글만)
        Long commentCount = queryFactory.select(comment.count())
                .from(comment)
                .where(comment.party.id.eq(partyId)
                        .and(comment.isDeleted.isFalse()))
                .fetchOne();

        // 대댓글 수 카운트 (부모 댓글의 삭제 여부와 관계없이 모든 대댓글)
        Long recommentCount = queryFactory.select(recomment.count())
                .from(recomment)
                .join(recomment.comment, comment)
                .where(comment.party.id.eq(partyId))
                .fetchOne();

        // null 방지
        commentCount = commentCount != null ? commentCount : 0L;
        recommentCount = recommentCount != null ? recommentCount : 0L;

        return commentCount + recommentCount;
    }
}
