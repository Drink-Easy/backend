package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllTastingNoteResponseDTO {

    int total;

    int red;
    int white;
    int sparkling;
    int rose;
    int etc;

    List<TastingNotePreviewResponseDTO> NotePriviewList;

    public static AllTastingNoteResponseDTO create(List<TastingNotePreviewResponseDTO> tastingNotePreviewResponseDTOList,
                                                                 int total, int red, int white, int sparkling, int rose, int etc){

        return AllTastingNoteResponseDTO.builder()
                .NotePriviewList(tastingNotePreviewResponseDTOList)
                .total(total)
                .red(red)
                .white(white)
                .sparkling(sparkling)
                .rose(rose)
                .etc(etc)
                .build();
    }
}
