package com.drinkeg.drinkeg.service.recommentService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Recomment;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface RecommentService {

    List<Recomment> findByCommentId(Long commentId);
    boolean existsByCommentId(Long commentId);
    void createRecomment(Long commentId, RecommentRequestDTO recommentRequest, PrincipalDetail principalDetail);
    void deleteRecomment(Long commentId, Long recommentId, PrincipalDetail principalDetail);
    long countByCommentId(Long commentId);
}
