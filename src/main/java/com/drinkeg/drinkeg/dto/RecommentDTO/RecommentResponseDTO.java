package com.drinkeg.drinkeg.dto.RecommentDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommentResponseDTO {
    private Long id;
    private Long commentId;
    private String memberName;
    private String content;
    private String timeAgo;
    private String createdDate;
    //private String url;
}
