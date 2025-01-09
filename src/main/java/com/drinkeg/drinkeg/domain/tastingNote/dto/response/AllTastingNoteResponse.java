package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllTastingNoteResponse {

    TastingNoteSortCountResponse sortCount;

    List<TastingNotePreviewResponse> NotePriviewList;

    @Builder
    public AllTastingNoteResponse(TastingNoteSortCountResponse sortCount, List<TastingNotePreviewResponse> NotePriviewList){
        this.sortCount = sortCount;
        this.NotePriviewList = NotePriviewList;
    }

    public static AllTastingNoteResponse create(TastingNoteSortCountResponse sortCount, List<TastingNotePreviewResponse> tastingNotePreviewResponseList){
        return AllTastingNoteResponse.builder()
                .sortCount(sortCount)
                .NotePriviewList(tastingNotePreviewResponseList)
                .build();
    }
}
