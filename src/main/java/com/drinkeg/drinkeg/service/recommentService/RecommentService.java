package com.drinkeg.drinkeg.service.recommentService;

import com.drinkeg.drinkeg.domain.Comment;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Recomment;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface RecommentService {

    List<Recomment> findByCommentId(Long commentId);
    boolean existsByCommentId(Long commentId);
    void createRecomment(Comment comment, RecommentRequestDTO recommentRequest, PrincipalDetail principalDetail);
    void deleteRecomment(PrincipalDetail principalDetail, Long recommentId);

}
