package com.drinkeg.drinkeg.service.recommentService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.RecommentConverter;
import com.drinkeg.drinkeg.domain.Comment;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Recomment;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.CommentRepository;
import com.drinkeg.drinkeg.repository.RecommentRepository;
import com.drinkeg.drinkeg.service.commentService.CommentService;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommentServiceImpl implements RecommentService{

    private final RecommentRepository recommentRepository;
    private final RecommentConverter recommentConverter;
    private final MemberService memberService;
    private final CommentService commentService;


    // 댓글 ID로 대댓글을 조회하는 메서드
    @Override
    public List<Recomment> findByCommentId(Long commentId) {
        return recommentRepository.findByCommentId(commentId);
    }

    // 특정 댓글(commentId)에 대댓글이 존재하는지 확인하는 메서드
    @Override
    public boolean existsByCommentId(Long commentId) {
        return recommentRepository.existsByCommentId(commentId);
    }

    @Override
    public void createRecomment(Long commentId, RecommentRequestDTO recommentRequest, PrincipalDetail principalDetail) {
        // 댓글 존재 여부 검증
        Comment comment = commentService.findByIdOrThrow(commentId);

        // 회원 존재 여부 검증
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        // 대댓글 엔티티 생성
        Recomment recomment = recommentConverter.fromRequest(recommentRequest, comment, member);

        // 대댓글 저장
        Recomment savedRecomment = recommentRepository.save(recomment);

    }

    @Override
    public void deleteRecomment(Long commentId, Long recommentId, PrincipalDetail principalDetail) {
        // 댓글 존재 여부 검증
        Comment comment = commentService.findByIdOrThrow(commentId);

        // 대댓글 존재 여부 검증
        Recomment recomment = recommentRepository.findById(recommentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECOMMENT_NOT_FOUND));

        // 현재 로그인 한 사용자가 작성자인지 확인
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);
        if(comment.getMember() == null || !comment.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 대댓글 삭제
        recommentRepository.delete(recomment);
    }
}
