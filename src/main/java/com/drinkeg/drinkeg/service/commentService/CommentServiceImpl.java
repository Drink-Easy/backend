package com.drinkeg.drinkeg.service.commentService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.CommentConverter;
import com.drinkeg.drinkeg.converter.RecommentConverter;
import com.drinkeg.drinkeg.domain.Comment;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.Recomment;
import com.drinkeg.drinkeg.dto.CommentRequestDTO;
import com.drinkeg.drinkeg.dto.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.RecommentResponseDTO;
import com.drinkeg.drinkeg.repository.CommentRepository;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.PartyRepository;
import com.drinkeg.drinkeg.repository.RecommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final CommentConverter commentConverter;
    private final RecommentConverter recommentConverter;
    private final RecommentRepository recommentRepository;

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO commentRequest) {

        // Party와 Member 존재 여부 검증
        Party party = partyRepository.findById(commentRequest.getPartyId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorStatus.PARTY_NOT_FOUND.getMessage()));

        Member member = memberRepository.findById(commentRequest.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

        // Comment 엔티티 생성
        Comment comment = commentConverter.toEntity(commentRequest, party, member);

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        // CommentResponseDTO로 변환
        return commentConverter.toResponse(savedComment);
    }


    @Override
    public RecommentResponseDTO createRecomment(Long commentId, RecommentRequestDTO recommentRequest) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorStatus.COMMENT_NOT_FOUND.getMessage()));

        // 회원 존재 여부 검증
        Member member = memberRepository.findById(recommentRequest.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

        // 대댓글 엔티티 생성
        Recomment recomment = recommentConverter.fromRequest(recommentRequest, comment, member);

        // 대댓글 저장
        Recomment savedRecomment = recommentRepository.save(recomment);

        // RecommentResponseDTO로 변환
        return recommentConverter.toResponse(savedRecomment);
    }

    @Override
    public void deleteComment(Long commentId) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        // 대댓글 여부 확인
        boolean hasRecomments = recommentRepository.existsByCommentId(commentId);

        if (hasRecomments) {
            throw new IllegalStateException("Cannot delete comment with recomments. Use updateCommentStatus instead.");
        } else {
            // 대댓글이 없는 경우: 댓글 삭제
            commentRepository.delete(comment);
        }
    }

    @Override
    public void updateCommentStatus(Long commentId) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        // 대댓글 여부 확인
        boolean hasRecomments = recommentRepository.existsByCommentId(commentId);

        if (hasRecomments) {
            // 대댓글이 있는 경우: isDeleted 상태를 true로 설정
            Comment updatedComment = commentConverter.setDeleted(comment);
            commentRepository.save(updatedComment);
        } else {
            throw new IllegalStateException("No recomments to update");
        }
    }


    @Override
    public void deleteRecomment(Long commentId, Long recommentId) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        // 대댓글 존재 여부 검증
        Recomment recomment = recommentRepository.findByIdAndCommentId(recommentId, commentId)
                .orElseThrow(() -> new EntityNotFoundException("Recomment not found"));

        // 대댓글 삭제
        recommentRepository.delete(recomment);
    }

}
