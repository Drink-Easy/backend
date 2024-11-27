package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.tastingNote.dto.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.tastingNote.dto.response.TastingNotePreviewResponseDTO;

import java.util.List;

public class TastingNoteConverter {

    public static AllTastingNoteResponseDTO toAllNoteResponseDTO(List<TastingNotePreviewResponseDTO> tastingNotePreviewResponseDTOList,
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