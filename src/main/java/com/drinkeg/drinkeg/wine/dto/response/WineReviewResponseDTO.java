package com.drinkeg.drinkeg.wine.dto.response;

import com.drinkeg.drinkeg.tastingNote.dto.response.TastingNotePreviewResponseDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class WineReviewResponseDTO {

    List<WineReviewDTO> wineReviews;
    boolean isLiked;

    @QueryProjection
    public WineReviewResponseDTO(List<WineReviewDTO> wineReviews, boolean isLiked) {
        this.wineReviews = wineReviews;
    }

    public static WineReviewResponseDTO create(List<WineReviewDTO> wineReviews, boolean isLiked){
        return WineReviewResponseDTO.builder()
                .wineReviews(wineReviews)
                .isLiked(isLiked)
                .build();
    }

    // 좋아요 여부 업데이트
    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
