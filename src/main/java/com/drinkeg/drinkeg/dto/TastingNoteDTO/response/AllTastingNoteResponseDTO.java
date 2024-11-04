package com.drinkeg.drinkeg.dto.TastingNoteDTO.response;

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
}
