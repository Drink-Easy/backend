package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePreviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TastingNoteConverter {

    // NoteRequestDTO를 TastingNote Entity로 변환
    // NotNull은 null이 아님을 보장하는 어노테이션
    public static TastingNote toTastingNoteEntity(NoteRequestDTO noteRequestDTO, @NotNull Member member, @NotNull Wine wine) {
        return TastingNote.builder()
                .member(member)

                .wine(wine)
                .color(noteRequestDTO.getColor())
                .tasteDate(noteRequestDTO.getTasteDate())

                .sugarContent(noteRequestDTO.getSugarContent())
                .acidity(noteRequestDTO.getAcidity())
                .tannin(noteRequestDTO.getTannin())
                .body(noteRequestDTO.getBody())
                .alcohol(noteRequestDTO.getAlcohol())

                .nose(noteRequestDTO.getNose())
                .palate(noteRequestDTO.getPalate())

                .satisfaction(noteRequestDTO.getSatisfaction())
                .review(noteRequestDTO.getReview())
                .build();
    }

    // TastingNote Entity를 NoteResponseDTO로 변환
    public static NoteResponseDTO toTastingNoteResponseDTO(TastingNote tastingNote) {
        Wine wine = tastingNote.getWine();

        return NoteResponseDTO.builder()
                .noteId(tastingNote.getId())
                .wineId(wine.getId())
                .wineName(wine.getName())
                .sort(wine.getSort())
                .area(wine.getArea())
                .imageUrl(wine.getImageUrl())

                .color(tastingNote.getColor())
                .tasteDate(tastingNote.getTasteDate())

                .sugarContent(tastingNote.getSugarContent())
                .acidity(tastingNote.getAcidity())
                .tannin(tastingNote.getTannin())
                .body(tastingNote.getBody())
                .alcohol(tastingNote.getAlcohol())

                .nose(tastingNote.getNose())
                .palate(tastingNote.getPalate())

                .satisfaction(tastingNote.getSatisfaction())
                .review(tastingNote.getReview())
                .build();
    }

    // TastingNote Entity를 NotePriviewResponseDTO로 변환
    public static NotePreviewResponseDTO toTastingNotePreviewDTO(TastingNote tastingNote) {
        return NotePreviewResponseDTO.builder()
                .noteId(tastingNote.getId())
                .name(tastingNote.getWine().getName())
                .imageUrl(tastingNote.getWine().getImageUrl())
                .build();
    }

    public static AllNoteResponseDTO toAllNoteResponseDTO(List<NotePreviewResponseDTO> notePreviewResponseDTOList,
                                                          int total, int red, int white, int sparkling, int rose, int etc){

        return AllNoteResponseDTO.builder()
                .NotePriviewList(notePreviewResponseDTOList)
                .total(total)
                .red(red)
                .white(white)
                .sparkling(sparkling)
                .rose(rose)
                .etc(etc)
                .build();
    }


}