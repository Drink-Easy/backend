package com.drinkeg.drinkeg.domain.recomment.service;


import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.drinkeg.drinkeg.domain.recomment.domain.Recomment;
import com.drinkeg.drinkeg.domain.recomment.dto.RecommentRequestDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface RecommentService {

    List<Recomment> findByCommentId(Long commentId);
    boolean existsByCommentId(Long commentId);
    void createRecomment(Comment comment, RecommentRequestDTO recommentRequest, PrincipalDetail principalDetail);
    void deleteRecomment(PrincipalDetail principalDetail, Long recommentId);
    long countByCommentId(Long commentId);
}
