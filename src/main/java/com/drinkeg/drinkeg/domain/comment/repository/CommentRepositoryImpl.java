package com.drinkeg.drinkeg.domain.comment.repository;
import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.drinkeg.drinkeg.domain.comment.domain.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public long countCommentsAndRecommentsByPartyId(Long partyId) {
        QComment comment = QComment.comment;
        Long count = queryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        comment.party.id.eq(partyId),
                        comment.isDeleted.isFalse()
                )
                .fetchOne();
        return count != null ? count : 0L;
    }


//    @Override
//    public List<Comment> findCommentsWithRecomments(Long partyId) {
//        QComment comment = QComment.comment;
//        QRecomment recomment = QRecomment.recomment;
//
//        return queryFactory.selectFrom(comment)
//                .leftJoin(recomment).on(comment.id.eq(recomment.comment.id))
//                .fetchJoin()
//                .distinct()
//                .fetch();
//    }
    @Override
    public List<Comment> findCommentsWithRecomments(Long partyId) {
        QComment comment = QComment.comment;
        return queryFactory
                .selectFrom(comment)
                .leftJoin(comment.children, comment).fetchJoin()
                .leftJoin(comment.member).fetchJoin()
                .where(
                        comment.party.id.eq(partyId),
                        comment.parent.isNull() // 루트댓글ㄹ
                )
                .distinct()
                .fetch();
    }
}
