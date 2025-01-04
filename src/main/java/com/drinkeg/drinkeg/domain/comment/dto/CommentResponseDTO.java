package com.drinkeg.drinkeg.domain.comment.dto;

import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.querydsl.core.annotations.QueryProjection;
import com.drinkeg.drinkeg.domain.recomment.dto.RecommentResponseDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    private Long id;
    private Long memberId;
    private String memberName;
    private String content;
    private boolean isDeleted;
    private List<RecommentResponseDTO> recomments;
    private String timeAgo;
    private String createdDate;
    //private String url;

    @QueryProjection
    public CommentResponseDTO(Long id, Long memberId, String memberName, String content, boolean isDeleted, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.content = content;
        this.isDeleted = isDeleted;
    }

    public static CommentResponseDTO fromEntity(Comment comment, String timeAgo, String createdDate, List<RecommentResponseDTO> recommentDTOs) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .memberId(comment.getMember().getId())
                .memberName(comment.getMember().getUsername())
                .content(comment.getContent())
                .isDeleted(comment.isDeleted())
                .timeAgo(timeAgo)
                .createdDate(createdDate)
                .recomments(recommentDTOs)
                .build();
    }
}
