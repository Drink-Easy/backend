package com.drinkeg.drinkeg.domain.comment.service;

import com.drinkeg.drinkeg.domain.recomment.repository.RecommentRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.drinkeg.drinkeg.domain.comment.dto.CommentRequestDTO;
import com.drinkeg.drinkeg.domain.comment.dto.CommentResponseDTO;
import com.drinkeg.drinkeg.domain.comment.repository.CommentRepository;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.service.MemberService;
import com.drinkeg.drinkeg.domain.party.domain.Party;
import com.drinkeg.drinkeg.domain.party.service.PartyService;
import com.drinkeg.drinkeg.domain.recomment.dto.RecommentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PartyService partyService;
    private final MemberService memberService;

    @Override
    public Comment findByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
    }

    @Override
    public long countCommentsAndRecommentsByPartyId(Long partyId) {
        return commentRepository.countCommentsAndRecommentsByPartyId(partyId);
    }



    @Override
    public void createComment(PrincipalDetail principalDetail, CommentRequestDTO commentRequest) {


        // Party와 Member 존재 여부 검증
        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        Party party = partyService.findPartyById(commentRequest.getPartyId());

        // Comment 엔티티 생성
        Comment comment = CommentRequestDTO.toEntity(commentRequest, party, foundMember);

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);
    }



    @Override
    public List<CommentResponseDTO> getCommentsByPartyId(Long partyId) {
        // 1. 파티 존재 여부 검증
        Party party = partyService.findPartyById(partyId);


        // 2. 댓글과 대댓글 조회
        List<Comment> comments = commentRepository.findCommentsWithRecomments(partyId);

        if (comments.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. DTO 매핑
        List<CommentResponseDTO> commentDTOs = comments.stream().map(comment -> {
            String timeAgo = calculateTimeAgo(comment.getCreatedAt());
            String createdDate = calculateCreatedDate(comment.getCreatedAt());

            // 대댓글 매핑
            List<RecommentResponseDTO> recommentDTOs = comment.getRecomments().stream().map(recomment -> {
                String recommentTimeAgo = calculateTimeAgo(recomment.getCreatedAt());
                String recommentCreatedDate = calculateCreatedDate(recomment.getCreatedAt());
                return RecommentResponseDTO.fromEntity(recomment, recommentTimeAgo, recommentCreatedDate);
            }).collect(Collectors.toList());

            return CommentResponseDTO.fromEntity(comment, timeAgo, createdDate, recommentDTOs);
        }).collect(Collectors.toList());

        return commentDTOs;
    }

    @Override
    public void deleteComment(PrincipalDetail principalDetail, Long commentId) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 현재 로그인 한 사용자가 작성자인지 확인
        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        if(comment.getMember() == null || !comment.getMember().equals(foundMember)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 대댓글 여부 확인
        boolean hasRecomments = !comment.getRecomments().isEmpty();

        if (hasRecomments) {
            throw new GeneralException(ErrorStatus.COMMENT_HAS_RECOMMENTS);
        } else {
            // 대댓글이 없는 경우: 댓글 삭제
            commentRepository.delete(comment);
        }
    }

    @Override
    public void updateCommentStatus(PrincipalDetail principalDetail, Long commentId) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 현재 로그인 한 사용자가 작성자인지 확인
        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        if(comment.getMember() == null || !comment.getMember().equals(foundMember)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 대댓글 여부 확인
        boolean hasRecomments = !comment.getRecomments().isEmpty();

        if (hasRecomments) {
            // 대댓글이 있는 경우: isDeleted 상태를 true로 설정
            Comment updatedComment = CommentResponseDTO.setDeleted(comment);
            commentRepository.save(updatedComment);
        } else {
            throw new GeneralException(ErrorStatus.COMMENT_HAS_NO_RECOMMENTS);
        }
    }




    // 시간 계산 메소드
    public String calculateTimeAgo(LocalDateTime createdAt) {
        // createdAt이 null인 경우 처리
        if (createdAt == null) {
            return null; // 또는 다른 적절한 기본 메시지
        }

        Duration duration = Duration.between(createdAt, LocalDateTime.now());
        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes + "분 전";
        }
        long hours = duration.toHours();
        if (hours < 24) {
            return hours + "시간 전";
        }
        long days = duration.toDays();
        return days + "일 전";
    }

    // 작성일자를 yyyy.MM.dd 형식으로 변환 메소드
    public String calculateCreatedDate(LocalDateTime createdAt) {
        // createdAt이 null인 경우 처리
        if (createdAt == null) {
            return "알 수 없음";
        }

        // 작성일자를 "yyyy.mm.dd" 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return createdAt.format(formatter);
    }
}
