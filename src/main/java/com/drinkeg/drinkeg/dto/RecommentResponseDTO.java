package com.drinkeg.drinkeg.dto;
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
    private Long memberId;
    private String content;
}
