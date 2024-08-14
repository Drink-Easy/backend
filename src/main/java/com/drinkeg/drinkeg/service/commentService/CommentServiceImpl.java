package com.drinkeg.drinkeg.service.commentService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.CommentConverter;
import com.drinkeg.drinkeg.converter.RecommentConverter;
import com.drinkeg.drinkeg.domain.Comment;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.Recomment;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentRequestDTO;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.CommentRepository;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.PartyRepository;
import com.drinkeg.drinkeg.repository.RecommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final RecommentConverter recommentConverter;
    private final RecommentRepository recommentRepository;
    // 검증을 위한 repository 접근
    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;

    @Override
    // 특정 모임에 대한 모든 댓글 및 대댓글 조회
    public List<CommentResponseDTO> getCommentsByPartyId(Long partyId) {
        // party 존재 여부 검증
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

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
    public void createComment(CommentRequestDTO commentRequest, Long memberId) {

        // Party와 Member 존재 여부 검증
        Party party = partyRepository.findById(commentRequest.getPartyId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // Comment 엔티티 생성
        Comment comment = commentConverter.toEntity(commentRequest, party, member);

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);
    }


    @Override
    public void createRecomment(Long commentId, RecommentRequestDTO recommentRequest, Member member) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 회원 존재 여부 검증
        Member foundMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 대댓글 엔티티 생성
        Recomment recomment = recommentConverter.fromRequest(recommentRequest, comment, foundMember);

        // 대댓글 저장
        Recomment savedRecomment = recommentRepository.save(recomment);

    }

    @Override
    public void deleteComment(Long commentId, Member member) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 현재 로그인 한 사용자가 작성자인지 확인
        if(comment.getMember() == null || !comment.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 대댓글 여부 확인
        boolean hasRecomments = recommentRepository.existsByCommentId(commentId);

        if (hasRecomments) {
            throw new GeneralException(ErrorStatus.COMMENT_HAS_RECOMMENTS);
        } else {
            // 대댓글이 없는 경우: 댓글 삭제
            commentRepository.delete(comment);
        }
    }

    @Override
    public void updateCommentStatus(Long commentId, Member member) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 현재 로그인 한 사용자가 작성자인지 확인
        if(comment.getMember() == null || !comment.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 대댓글 여부 확인
        boolean hasRecomments = recommentRepository.existsByCommentId(commentId);

        if (hasRecomments) {
            // 대댓글이 있는 경우: isDeleted 상태를 true로 설정
            Comment updatedComment = commentConverter.setDeleted(comment);
            commentRepository.save(updatedComment);
        } else {
            throw new GeneralException(ErrorStatus.COMMENT_HAS_NO_RECOMMENTS);
        }
    }


    @Override
    public void deleteRecomment(Long commentId, Long recommentId, Member member) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 대댓글 존재 여부 검증
        Recomment recomment = recommentRepository.findById(recommentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RECOMMENT_NOT_FOUND));

        // 현재 로그인 한 사용자가 작성자인지 확인
        if(comment.getMember() == null || !comment.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 대댓글 삭제
        recommentRepository.delete(recomment);
    }

}
