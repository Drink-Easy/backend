package com.drinkeg.drinkeg.comment.service;

import com.drinkeg.drinkeg.comment.domain.Comment;
import com.drinkeg.drinkeg.comment.dto.CommentRequestDTO;
import com.drinkeg.drinkeg.comment.dto.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    Comment findByIdOrThrow(Long commentId);

    long countCommentsAndRecommentsByPartyId(Long partyId);

    void createComment(PrincipalDetail principalDetail, CommentRequestDTO commentRequest);

    void deleteComment(PrincipalDetail principalDetail, Long commentId);

    void updateCommentStatus(PrincipalDetail principalDetail, Long commentId);

    List<CommentResponseDTO> getCommentsByPartyId(Long partyId) ;

    String calculateTimeAgo(LocalDateTime createdAt);

    String calculateCreatedDate(LocalDateTime createdAt);
}
