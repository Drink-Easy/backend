package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.TastingNoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.TastingNotePreviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.TastingNoteResponseDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TastingNoteConverter {

    // NoteRequestDTO를 TastingNote Entity로 변환
    // NotNull은 null이 아님을 보장하는 어노테이션
    public static com.drinkeg.drinkeg.domain.TastingNote toTastingNoteEntity(TastingNoteRequestDTO tastingNote, @NotNull Member member, @NotNull Wine wine) {
        return com.drinkeg.drinkeg.domain.TastingNote.builder()
                .member(member)

                .wine(wine)
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

    // TastingNote Entity를 NoteResponseDTO로 변환
    public static TastingNoteResponseDTO toTastingNoteResponseDTO(com.drinkeg.drinkeg.domain.TastingNote tastingNote) {
        Wine wine = tastingNote.getWine();

        return TastingNoteResponseDTO.builder()
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
    public static TastingNotePreviewResponseDTO toTastingNotePreviewDTO(com.drinkeg.drinkeg.domain.TastingNote tastingNote) {
        return TastingNotePreviewResponseDTO.builder()
                .noteId(tastingNote.getId())
                .name(tastingNote.getWine().getName())
                .imageUrl(tastingNote.getWine().getImageUrl())
                .build();
    }

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