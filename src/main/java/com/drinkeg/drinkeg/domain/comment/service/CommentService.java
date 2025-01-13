package com.drinkeg.drinkeg.domain.comment.service;

import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.drinkeg.drinkeg.domain.comment.dto.CommentRequestDTO;
import com.drinkeg.drinkeg.domain.comment.dto.CommentResponseDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    Comment findByIdOrThrow(Long commentId);

    List<CommentResponseDTO> getCommentsByPartyId(Long partyId);

    long countCommentsAndRecommentsByPartyId(Long partyId);

    void createComment(PrincipalDetail principalDetail, CommentRequestDTO commentRequest);

    void updateComment(PrincipalDetail principalDetail, Long commentId, String newContent);

    void softDeleteComment(PrincipalDetail principalDetail, Long commentId);

    void hardDeleteComment(PrincipalDetail principalDetail, Long commentId);

    String calculateTimeAgo(LocalDateTime createdAt);

    String calculateCreatedDate(LocalDateTime createdAt);
}
