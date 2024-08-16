package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineReviewResponseDTO;

public class WineConverter {

    // 검색한 와인을 노트 와인 응답 DTO로 변환
    public static SearchWineResponseDTO toSearchWineDTO(Wine wine) {
        return SearchWineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())

                .price(((wine.getPrice() * 1300) / 1000) * 1000)

                .rating(Math.max(wine.getRating(), wine.getWineNote().getRating()))
                .build();
    }

    // 검색한 와인을 노트 와인 응답 DTO로 변환
    public static WineResponseDTO toWineResponseDTO(Wine wine) {
        WineNote wineNote = wine.getWineNote();

        return WineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())

                .price(((wine.getPrice() * 1300) / 100) * 100)
                .sort(wine.getSort())
                .area(wine.getArea())

                .sugarContent(wineNote.getSugarContent())
                .acidity(wineNote.getAcidity())
                .tannin(wineNote.getTannin())
                .body(wineNote.getBody())
                .alcohol(wineNote.getAlcohol())

                .scentAroma(wineNote.getScentAroma())
                .scentTaste(wineNote.getScentTaste())
                .scentFinish(wineNote.getScentFinish())

                .rating(Math.max(wine.getRating(), wineNote.getRating()))
                .build();
    }

    public static WineReviewResponseDTO toWineReviewResPonseDTO(TastingNote tastingNote){

        return WineReviewResponseDTO.builder()
                .name(tastingNote.getMember().getName())
                .satisfaction(tastingNote.getSatisfaction())
                .review(tastingNote.getReview())
                .build();

    }
}
