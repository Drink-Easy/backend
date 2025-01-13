package com.drinkeg.drinkeg.domain.recomment.service;

import com.drinkeg.drinkeg.domain.comment.repository.CommentRepository;
import com.drinkeg.drinkeg.domain.recomment.dto.RecommentResponseDTO;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.drinkeg.drinkeg.domain.recomment.RecommentConverter;
import com.drinkeg.drinkeg.domain.member.domain.Member;

import com.drinkeg.drinkeg.domain.recomment.domain.Recomment;
import com.drinkeg.drinkeg.domain.recomment.dto.RecommentRequestDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.service.MemberService;
import com.drinkeg.drinkeg.domain.recomment.repository.RecommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommentServiceImpl implements RecommentService{
//
//    private final RecommentRepository recommentRepository;
//    private final RecommentConverter recommentConverter;
//    private final MemberService memberService;
//    private final CommentRepository commentRepository;
//
//
//    // 특정 댓글에 포함되는 대댓글의 개수를 반환하는 메서드
//    @Override
//    public long countByCommentId(Long commentId) {
//        //Optional이 비어있으면 0 반환(.orElse(0L))
//        return recommentRepository.countByCommentId(commentId).orElse(0L);
//    }
//
//
//
//    @Override
//    @Transactional
//    public void createRecomment(Comment comment, RecommentRequestDTO recommentRequest, PrincipalDetail principalDetail) {
//
//        // 회원 존재 여부 검증
//        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);
//
//        // 대댓글 엔티티 생성
//        Recomment recomment = recommentConverter.fromRequest(recommentRequest, comment, member);
//
//        // 부모 엔티티에 대댓글 추가 (CascadeType.ALL로 인해 자식도 자동 저장됨)
//        //comment.addRecomment(recomment);
//
//        // 부모 엔티티 저장
//        commentRepository.save(comment); // 자식은 자동 저장됨
//
//    }
//
//    @Override
//    @Transactional
//    public void deleteRecomment(PrincipalDetail principalDetail, Long recommentId) {
//
//        // 대댓글 존재 여부 검증
//        Recomment recomment = recommentRepository.findById(recommentId)
//                .orElseThrow(() -> new GeneralException(ErrorStatus.RECOMMENT_NOT_FOUND));
//
//        // 현재 로그인 한 사용자가 작성자인지 확인
//        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);
//        if(recomment.getMember() == null || !recomment.getMember().equals(member)) {
//            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
//        }
//
//        // 부모(Comment) 엔티티에서 해당 대댓글 제거
//        Comment parentComment = recomment.getComment();
//        if (parentComment != null) {
//            parentComment.removeRecomment(recomment); // 부모의 컬렉션에서 제거
//        }
//    }
}
