package com.drinkeg.drinkeg.domain.recomment.dto;
import com.drinkeg.drinkeg.domain.recomment.domain.Recomment;
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
    private Long memberId;
    private String content;
    private String timeAgo;
    private String createdDate;
    //private String url;

    public static RecommentResponseDTO fromEntity(Recomment recomment, String timeAgo, String createdDate) {
        return RecommentResponseDTO.builder()
                .id(recomment.getId())
                .commentId(recomment.getComment().getId())
                .memberId(recomment.getMember().getId())
                .memberName(recomment.getMember().getUsername())
                .content(recomment.getContent())
                .timeAgo(timeAgo)
                .createdDate(createdDate)
                .build();
    }
}
