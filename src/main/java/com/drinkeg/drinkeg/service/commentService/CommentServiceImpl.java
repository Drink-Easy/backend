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

import java.util.List;
import java.util.stream.Collectors;

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
    // 특정 모임에 대한 모든 댓글 및 대댓글 조회
    public List<CommentResponseDTO> getCommentsByPartyId(Long partyId) {
        // 특정 모임의 댓글 조회
        List<Comment> comments = commentRepository.findByPartyId(partyId);

        // 댓글을 DTO로 변환하면서 대댓글도 함께 처리
        List<CommentResponseDTO> commentResponseDTOs = comments.stream().map(comment -> {
            // 댓글에 연결된 대댓글들 조회
            List<Recomment> recomments = recommentRepository.findByCommentId(comment.getId());

            // 대댓글들을 DTO로 변환
            List<RecommentResponseDTO> recommentResponseDTOs = recomments.stream()
                    .map(recommentConverter::toResponse)
                    .collect(Collectors.toList());

            // 댓글 DTO 변환
            CommentResponseDTO commentDTO = commentConverter.toResponse(comment);

            // 만약 댓글이 삭제되었고, 대댓글이 존재한다면 프론트에 전달할 메시지 설정
            if (comment.isDeleted() && !recommentResponseDTOs.isEmpty()) {
                commentDTO.setContent("삭제된 댓글입니다.");
            }

            // DTO에 대댓글 리스트 추가
            commentDTO.setRecomments(recommentResponseDTOs);
            return commentDTO;
        }).collect(Collectors.toList());

        return commentResponseDTOs;
    }


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
