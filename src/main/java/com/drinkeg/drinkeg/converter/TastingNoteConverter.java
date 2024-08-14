package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePriviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import jakarta.validation.constraints.NotNull;

public class TastingNoteConverter {

    // NoteRequestDTO를 TastingNote Entity로 변환
    // NotNull은 null이 아님을 보장하는 어노테이션
    public static TastingNote toTastingNoteEntity(NoteRequestDTO noteRequestDTO, @NotNull Member member, @NotNull Wine wine) {
        return TastingNote.builder()
                .member(member)

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

    // TastingNote Entity를 NoteResponseDTO로 변환
    public static NoteResponseDTO toTastingNoteResponseDTO(TastingNote tastingNote) {
        return NoteResponseDTO.builder()
                .noteId(tastingNote.getId())
                .wineId(tastingNote.getWine().getId())
                .name(tastingNote.getWine().getName())
                .picture(tastingNote.getWine().getImageUrl())

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

    // TastingNote Entity를 NotePriviewResponseDTO로 변환
    public static NotePriviewResponseDTO toTastingNotePreviewDTO(TastingNote tastingNote) {
        return NotePriviewResponseDTO.builder()
                .noteId(tastingNote.getId())
                .name(tastingNote.getWine().getName())
                .picture(tastingNote.getWine().getImageUrl())
                .build();
    }

}