package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.dto.WineDTO.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.WineReviewResponseDTO;

public class WineConverter {

    // 검색한 와인을 노트 와인 응답 DTO로 변환
    public static SearchWineResponseDTO toNoteSearchWineDTO(Wine wine) {
        return SearchWineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())
                .build();
    }

    // 검색한 와인을 노트 와인 응답 DTO로 변환
    public static WineResponseDTO toWineResponseDTO(Wine wine) {
        WineNote wineNote = wine.getWineNote();
        float rating;
        if(wine.getRating() >= wineNote.getRating()){
            rating = wine.getRating();
        }
        else {
            rating = wineNote.getRating();
        }

        return WineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())

                .sugarContent(wineNote.getSugarContent())
                .acidity(wineNote.getAcidity())
                .tannin(wineNote.getTannin())
                .body(wineNote.getBody())
                .alcohol(wineNote.getAlcohol())

                .scentAroma(wineNote.getScentAroma())
                .scentTaste(wineNote.getScentTaste())
                .scentFinish(wineNote.getScentFinish())

                .rating(rating)
                .build();
    }

    public static WineReviewResponseDTO toWineReviewResPonseDTO(TastingNote tastingNote){

        return WineReviewResponseDTO.builder()
                .satisfaction(tastingNote.getSatisfaction())
                .review(tastingNote.getReview())
                .build();

    }
}
