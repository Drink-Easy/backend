package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.*;
import com.drinkeg.drinkeg.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.tastingNote.dto.request.TastingNoteRequestDTO;
import com.drinkeg.drinkeg.tastingNote.dto.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.tastingNote.dto.response.TastingNotePreviewResponseDTO;
import com.drinkeg.drinkeg.tastingNote.dto.response.TastingNoteResponseDTO;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TastingNoteConverter {

    // NoteRequestDTO를 TastingNote Entity로 변환
    // NotNull은 null이 아님을 보장하는 어노테이션
    public static TastingNote toTastingNoteEntity(TastingNoteRequestDTO tastingNoteRequestDTO, @NotNull Member member, @NotNull Wine wine) {
        TastingNote tastingNote = TastingNote.builder()
                .member(member)

                .wine(wine)
                .color(tastingNoteRequestDTO.getColor())
                .tasteDate(tastingNoteRequestDTO.getTasteDate())

                .sugarContent(tastingNoteRequestDTO.getSugarContent())
                .acidity(tastingNoteRequestDTO.getAcidity())
                .tannin(tastingNoteRequestDTO.getTannin())
                .body(tastingNoteRequestDTO.getBody())
                .alcohol(tastingNoteRequestDTO.getAlcohol())


                .satisfaction(tastingNoteRequestDTO.getSatisfaction())
                .review(tastingNoteRequestDTO.getReview())
                .build();
        for(String noseElement : tastingNoteRequestDTO.getNose()){
            tastingNote.addNoseElement(noseElement);
        }
        return tastingNote;
    }

    // TastingNote Entity를 NoteResponseDTO로 변환
    public static TastingNoteResponseDTO toTastingNoteResponseDTO(TastingNote tastingNote) {
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

                .noseList(tastingNote.getNoseList().stream()
                        .map(nose -> {
                            Map<Long, String> noseMap = new HashMap<>();
                            noseMap.put(nose.getId(), nose.getNoseElement());
                            return noseMap;
                        })
                        .collect(Collectors.toList()))

                .satisfaction(tastingNote.getSatisfaction())
                .review(tastingNote.getReview())
                .build();

    }

    // TastingNote Entity를 NotePriviewResponseDTO로 변환
    public static TastingNotePreviewResponseDTO toTastingNotePreviewDTO(TastingNote tastingNote) {
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