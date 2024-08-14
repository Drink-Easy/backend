package com.drinkeg.drinkeg.dto.RecommentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommentResponseDTO {
    private Long id;
    private Long commentId;
    private String memberName;
    private String content;
}
