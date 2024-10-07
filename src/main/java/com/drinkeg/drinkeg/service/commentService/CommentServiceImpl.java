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
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.CommentRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.partyService.PartyService;
import com.drinkeg.drinkeg.service.recommentService.RecommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;

    private final RecommentConverter recommentConverter;
    private final RecommentService recommentService;
    private final PartyService partyService;
    private final MemberService memberService;

    @Override
    public Comment findByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
    }


    @Override
    public List<CommentResponseDTO> getCommentsByPartyId(Long partyId) {
        // party 존재 여부 검증
        Party party = partyService.findPartyById(partyId);

        // 특정 모임의 댓글 조회
        List<Comment> comments = commentRepository.findByPartyId(partyId);

        // 댓글을 DTO로 변환하면서 대댓글도 함께 처리
        return comments.stream().map(comment -> {

            CommentResponseDTO commentDTO = commentConverter.toResponse(comment);

            // 시간 계산하여 timeAgo 필드에 설정
            String timeAgo = calculateTimeAgo(comment.getCreatedAt());
            commentDTO.setTimeAgo(timeAgo);

            // 작성일자 계산하여 createdDate 필드에 설정
            String createdDate = calculateCreatedDate(comment.getCreatedAt());
            commentDTO.setCreatedDate(createdDate);

            // 대댓글 처리
            List<Recomment> recomments = recommentService.findByCommentId(comment.getId());
            List<RecommentResponseDTO> recommentDTOs = recomments.stream()
                    .map(recomment -> {
                        RecommentResponseDTO recommentDTO = recommentConverter.toResponse(recomment);

                        // 시간 계산하여 timeAgo 필드에 설정
                        String recommentTimeAgo = calculateTimeAgo(recomment.getCreatedAt());
                        recommentDTO.setTimeAgo(recommentTimeAgo);

                        // 작성일자 계산하여 createdDate 필드에 설정
                        String recommentCreatedDate = calculateCreatedDate(recomment.getCreatedAt());
                        recommentDTO.setCreatedDate(recommentCreatedDate);

                        return recommentDTO; // 변환된 RecommentResponseDTO 반환
                    })
                    .collect(Collectors.toList());

            // DTO에 대댓글 리스트 추가
            commentDTO.setRecomments(recommentDTOs);

            return commentDTO;
        }).collect(Collectors.toList());
    }



    @Override
    public void createComment(PrincipalDetail principalDetail, CommentRequestDTO commentRequest) {


        // Party와 Member 존재 여부 검증
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);
        Party party = partyService.findPartyById(commentRequest.getPartyId());

        // Comment 엔티티 생성
        Comment comment = commentConverter.toEntity(commentRequest, party, foundMember);

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);
    }



    @Override
    public void deleteComment(PrincipalDetail principalDetail, Long commentId) {
        // 댓글 존재 여부 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 현재 로그인 한 사용자가 작성자인지 확인
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);
        if(comment.getMember() == null || !comment.getMember().equals(foundMember)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 대댓글 여부 확인
        boolean hasRecomments = recommentService.existsByCommentId(commentId);

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
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);
        if(comment.getMember() == null || !comment.getMember().equals(foundMember)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_COMMENT);
        }

        // 대댓글 여부 확인
        boolean hasRecomments = recommentService.existsByCommentId(commentId);

        if (hasRecomments) {
            // 대댓글이 있는 경우: isDeleted 상태를 true로 설정
            Comment updatedComment = commentConverter.setDeleted(comment);
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
