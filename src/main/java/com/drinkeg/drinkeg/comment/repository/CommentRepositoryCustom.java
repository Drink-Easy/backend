package com.drinkeg.drinkeg.comment.repository;

import com.drinkeg.drinkeg.comment.dto.CommentResponseDTO;

import java.util.List;

public interface CommentRepositoryCustom {
    long countCommentsAndRecommentsByPartyId(Long partyId);
    List<CommentResponseDTO> findCommentsWithRecomments(Long partyId);
}
