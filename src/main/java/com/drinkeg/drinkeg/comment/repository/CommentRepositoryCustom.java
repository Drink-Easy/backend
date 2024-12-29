package com.drinkeg.drinkeg.comment.repository;

public interface CommentRepositoryCustom {
    long countCommentsAndRecommentsByPartyId(Long partyId);
}
