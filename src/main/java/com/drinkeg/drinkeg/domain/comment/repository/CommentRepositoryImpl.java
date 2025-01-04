package com.drinkeg.drinkeg.domain.comment.repository;

import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.drinkeg.drinkeg.domain.comment.domain.QComment;
import com.drinkeg.drinkeg.domain.comment.dto.CommentResponseDTO;
import com.drinkeg.drinkeg.domain.recomment.domain.QRecomment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.drinkeg.drinkeg.domain.member.domain.QMember.member;

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

    @Override
    public List<Comment> findCommentsWithRecomments(Long partyId) {
        QComment comment = QComment.comment;
        QRecomment recomment = QRecomment.recomment;

        return queryFactory.selectFrom(comment)
                .leftJoin(comment.recomments, recomment)
                .fetchJoin()
                .where(
                        comment.party.id.eq(partyId)
                )
                .distinct()
                .fetch();
    }
}
