package com.drinkeg.drinkeg.service.commentService;

import com.drinkeg.drinkeg.domain.Comment;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentRequestDTO;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    Comment findByIdOrThrow(Long commentId);

    long countCommentsAndRecommentsByPartyId(Long partyId);

    void createComment(CommentRequestDTO commentRequest, PrincipalDetail principalDetail);

    void deleteComment(Long commentId, PrincipalDetail principalDetail);

    void updateCommentStatus(Long commentId, PrincipalDetail principalDetail);

    List<CommentResponseDTO> getCommentsByPartyId(Long partyId) ;

    String calculateTimeAgo(LocalDateTime createdAt);

    String calculateCreatedDate(LocalDateTime createdAt);
}
