package com.drinkeg.drinkeg.comment.dto;

import com.drinkeg.drinkeg.recomment.dto.*;
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
    private String memberName;
    private String content;
    private boolean isDeleted;
    private List<RecommentResponseDTO> recomments;
    private String timeAgo;
    private String createdDate;
    //private String url;
}
