package com.drinkeg.drinkeg.dto;

import lombok.*;

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
    private String content;
    private boolean isDeleted;
    private List<RecommentResponseDTO> recomments;
}
