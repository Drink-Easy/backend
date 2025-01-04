package com.drinkeg.drinkeg.domain.comment.repository;


import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.drinkeg.drinkeg.domain.comment.dto.CommentResponseDTO;

import java.util.List;

public interface CommentRepositoryCustom {
    long countCommentsAndRecommentsByPartyId(Long partyId);
    List<Comment> findCommentsWithRecomments(Long partyId);
}
