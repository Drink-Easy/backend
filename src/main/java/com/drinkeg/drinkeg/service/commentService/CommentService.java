package com.drinkeg.drinkeg.service.commentService;

import com.drinkeg.drinkeg.dto.CommentRequestDTO;
import com.drinkeg.drinkeg.dto.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.RecommentResponseDTO;

public interface CommentService {
    CommentResponseDTO createComment(CommentRequestDTO commentRequest);

    RecommentResponseDTO createRecomment(Long commentId, RecommentRequestDTO recommentRequest);

    void deleteComment(Long commentId);

    void updateCommentStatus(Long commentId);

    void deleteRecomment(Long commentId, Long recommentId);

}
