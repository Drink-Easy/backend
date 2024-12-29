package com.drinkeg.drinkeg.comment.dto;

import com.drinkeg.drinkeg.recomment.dto.*;
import com.querydsl.core.annotations.QueryProjection;
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
    private Long partyId;
    private Long memberId;
    private String memberName;
    private String content;
    private boolean isDeleted;
    private List<RecommentResponseDTO> recomments;
    private String timeAgo;
    private String createdDate;
    //private String url;

    @QueryProjection
    public CommentResponseDTO(Long id, Long partyId, Long memberId, String memberName, String content, boolean isDeleted, LocalDateTime createdAt) {
        this.id = id;
        this.partyId = partyId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.content = content;
        this.isDeleted = isDeleted;
    }
}
