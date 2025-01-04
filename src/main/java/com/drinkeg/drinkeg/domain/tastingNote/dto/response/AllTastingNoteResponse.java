package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllTastingNoteResponse {

    int total;

    int red;
    int white;
    int sparkling;
    int rose;
    int etc;

    List<TastingNotePreviewResponse> NotePriviewList;

    public static AllTastingNoteResponse create(List<TastingNotePreviewResponse> tastingNotePreviewResponseList,
                                                int total, int red, int white, int sparkling, int rose, int etc){

        return AllTastingNoteResponse.builder()
                .NotePriviewList(tastingNotePreviewResponseList)
                .total(total)
                .red(red)
                .white(white)
                .sparkling(sparkling)
                .rose(rose)
                .etc(etc)
                .build();
    }
}
