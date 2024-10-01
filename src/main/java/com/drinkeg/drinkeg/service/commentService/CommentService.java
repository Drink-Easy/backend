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

    void createComment(PrincipalDetail principalDetail, CommentRequestDTO commentRequest);

    void deleteComment(PrincipalDetail principalDetail, Long commentId);

    void updateCommentStatus(PrincipalDetail principalDetail, Long commentId);

    List<CommentResponseDTO> getCommentsByPartyId(Long partyId) ;

    String calculateTimeAgo(LocalDateTime createdAt);

    String calculateCreatedDate(LocalDateTime createdAt);

}
