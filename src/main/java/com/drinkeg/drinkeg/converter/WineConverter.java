package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteWineResponseDTO;

public class WineConverter {

    // 검색한 와인을 노트 와인 응답 DTO로 변환
    public static NoteWineResponseDTO toNoteSearchWineDTO(Wine wine) {
        return NoteWineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .picture(wine.getImageUrl())
                .build();
    }
}
