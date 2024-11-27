package com.drinkeg.drinkeg.wineClass.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class WineClassResponseDTO {
    private Long id;
    private String category;
    private String title;
    private String thumbnailUrl;
    private float progress;

    @QueryProjection
    public WineClassResponseDTO(Long id, String category, String title, String thumbnailUrl, float progress) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.progress = progress;
    }
}
