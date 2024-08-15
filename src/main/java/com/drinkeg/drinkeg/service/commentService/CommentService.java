package com.drinkeg.drinkeg.service.commentService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentRequestDTO;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    void createComment(CommentRequestDTO commentRequest, Long memberId);

    void createRecomment(Long commentId, RecommentRequestDTO recommentRequest, Member member);

    void deleteComment(Long commentId, Member member);

    void updateCommentStatus(Long commentId, Member member);

    void deleteRecomment(Long commentId, Long recommentId, Member member);

    List<CommentResponseDTO> getCommentsByPartyId(Long partyId) ;

    String calculateTimeAgo(LocalDateTime createdAt);

    String calculateCreatedDate(LocalDateTime createdAt);
}
