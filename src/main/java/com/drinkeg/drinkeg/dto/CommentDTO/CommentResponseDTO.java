package com.drinkeg.drinkeg.dto.CommentDTO;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentResponseDTO;
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
    private String memberName;
    private String content;
    private boolean isDeleted;
    private List<RecommentResponseDTO> recomments;
    private String timeAgo;
    private String createdDate;
    //private String url;
}
