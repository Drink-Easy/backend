package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import com.drinkeg.drinkeg.global.dto.PageResponse;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllTastingNoteResponse {

    TastingNoteSortCountResponse sortCount;

    PageResponse<TastingNotePreviewResponse> pageResponse;

    @Builder
    public AllTastingNoteResponse(TastingNoteSortCountResponse sortCount, PageResponse<TastingNotePreviewResponse> pageResponse){
        this.sortCount = sortCount;
        this.pageResponse = pageResponse;
    }

    public static AllTastingNoteResponse create(TastingNoteSortCountResponse sortCount, PageResponse<TastingNotePreviewResponse> pageResponse){
        return AllTastingNoteResponse.builder()
                .sortCount(sortCount)
                .pageResponse(pageResponse)
                .build();
    }
}
