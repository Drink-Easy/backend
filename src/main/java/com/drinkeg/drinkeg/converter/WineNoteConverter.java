package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.dto.WineNoteDTO.WineNoteResponseDTO;

public class WineNoteConverter {

    // 검색한 와인을 노트 와인 응답 DTO로 변환
    public static WineNoteResponseDTO toWineNoteResponseDTO(WineNote wineNote) {
        return WineNoteResponseDTO.builder()
                .wineId(wineNote.getWine().getId())
                .sugarContent(wineNote.getSugarContent())
                .acidity(wineNote.getAcidity())
                .tannin(wineNote.getTannin())
                .body(wineNote.getBody())
                .alcohol(wineNote.getAlcohol())

                .scentAroma(wineNote.getScentAroma())
                .scentTaste(wineNote.getScentTaste())
                .scentFinish(wineNote.getScentFinish())
                .build();
    }

}
