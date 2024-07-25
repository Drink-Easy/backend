package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteResponseDTO;
import jakarta.validation.constraints.NotNull;

public class TastingNoteConverter {

    public static TastingNote toTastingNoteEntity(NoteRequestDTO noteRequestDTO, @NotNull Wine wine) {
        return TastingNote.builder()
                .wine(wine)
                .color(noteRequestDTO.getColor())

                .sugarContent(noteRequestDTO.getSugarContent())
                .acidity(noteRequestDTO.getAcidity())
                .tannin(noteRequestDTO.getTannin())
                .body(noteRequestDTO.getBody())
                .alcohol(noteRequestDTO.getAlcohol())

                .scentAroma(noteRequestDTO.getScentAroma())
                .scentTaste(noteRequestDTO.getScentTaste())
                .scentFinish(noteRequestDTO.getScentFinish())

                .satisfaction(noteRequestDTO.getSatisfaction())
                .memo(noteRequestDTO.getMemo())
                .build();
    }

    public static NoteResponseDTO toTastingNoteResponseDTO(TastingNote tastingNote) {
        return NoteResponseDTO.builder()
                .wineId(tastingNote.getWine().getId())
                .name(tastingNote.getWine().getName())
                .picture(tastingNote.getWine().getPicture())

                .color(tastingNote.getColor())

                .sugarContent(tastingNote.getSugarContent())
                .acidity(tastingNote.getAcidity())
                .tannin(tastingNote.getTannin())
                .body(tastingNote.getBody())
                .alcohol(tastingNote.getAlcohol())

                .scentAroma(tastingNote.getScentAroma())
                .scentTaste(tastingNote.getScentTaste())
                .scentFinish(tastingNote.getScentFinish())

                .satisfaction(tastingNote.getSatisfaction())
                .memo(tastingNote.getMemo())
                .build();
    }

}