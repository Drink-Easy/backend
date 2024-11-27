package com.drinkeg.drinkeg.wineLecture.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
public class WineLectureResponseDTO {
    private Long id;
    private String title;
    private String content;
    private boolean isCompleted;

    @QueryProjection
    public WineLectureResponseDTO(Long id, String title, String content, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isCompleted = isCompleted;
    }
}
