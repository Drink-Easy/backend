package com.drinkeg.drinkeg.domain.comment.service;

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
import jakarta.transaction.Transactional;
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



    @Transactional
    @Override
    public void createComment(PrincipalDetail principalDetail, CommentRequestDTO commentRequest) {
        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        Party party = partyService.findPartyById(commentRequest.getPartyId());

        Comment parentComment = null;
        if (commentRequest.getParentCommentId() != null) {
            parentComment = commentRepository.findById(commentRequest.getParentCommentId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
        }

        Comment newComment = CommentRequestDTO.toEntity(commentRequest, party, foundMember, parentComment);
        // parent.addChild는 내부에서 이미 처리

        commentRepository.save(newComment);
    }



    @Override
    public List<CommentResponseDTO> getCommentsByPartyId(Long partyId) {
        // 파티 검증
        Party party = partyService.findPartyById(partyId);

        // 루트 댓글 + 대댓글 fetch
        List<Comment> rootComments = commentRepository.findCommentsWithRecomments(partyId);
        if (rootComments.isEmpty()) {
            return Collections.emptyList();
        }

        return rootComments.stream()
                .map(parent -> {
                    // 부모 DTO
                    CommentResponseDTO parentDTO = CommentResponseDTO.fromEntity(
                            parent,
                            calculateTimeAgo(parent.getCreatedAt()),
                            calculateCreatedDate(parent.getCreatedAt())
                    );

                    // 자식 중 isDeleted=false만
                    List<CommentResponseDTO> childDTOs = parent.getChildren().stream()
                            .filter(child -> !child.isDeleted())
                            .map(child -> {
                                return CommentResponseDTO.fromEntity(
                                        child,
                                        calculateTimeAgo(child.getCreatedAt()),
                                        calculateCreatedDate(child.getCreatedAt())
                                );
                            })
                            .collect(Collectors.toList());

                    parentDTO.setChildren(childDTOs);
                    return parentDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countCommentsAndRecommentsByPartyId(Long partyId) {
        partyService.findPartyById(partyId); // 존재여부 검증
        return commentRepository.countCommentsAndRecommentsByPartyId(partyId);
    }

    @Transactional
    @Override
    public void updateComment(PrincipalDetail principalDetail, Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        if (comment.getMember() == null || !comment.getMember().equals(foundMember)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 소프트 딜리트된 댓글이면 수정 불가
        if (comment.isDeleted()) {
            throw new GeneralException(ErrorStatus.COMMENT_NOT_FOUND);
        }

        //엔티티 비즈니스 로직
        comment.updateContent(newContent);
    }

    @Transactional
    @Override
    public void softDeleteComment(PrincipalDetail principalDetail, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        if (comment.getMember() == null || !comment.getMember().equals(foundMember)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        //엔티티 비즈니스 로직
        comment.softDelete();
    }

    @Transactional
    @Override
    public void hardDeleteComment(PrincipalDetail principalDetail, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        if (comment.getMember() == null || !comment.getMember().equals(foundMember)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 대댓글 존재 시 정책 결정
        // 여기서는 Cascade(연쇄 삭제)로 대댓글도 함께 삭제됨
        commentRepository.delete(comment);
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
