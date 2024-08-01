package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.WineNews;
import com.drinkeg.drinkeg.dto.WineNewsDTO.request.WineNewsRequestDTO;
import com.drinkeg.drinkeg.dto.WineNewsDTO.response.WineNewsResponseDTO;

public class WineNewsConverter {
    // WineNews를 WineNewsResponseDTO로 변환
    public static WineNewsResponseDTO toWineNewsResponseDTO(WineNews wineNews) {
        return WineNewsResponseDTO.builder()
                .id(wineNews.getId())
                .title(wineNews.getTitle())
                .cardNewsImg(wineNews.getCardNewsImg())
                .build();
    }

    // WineNewsResponseDTO를 WineNews로 변환
    public static WineNews toWineNews(WineNewsRequestDTO wineNewsRequestDTO) {
        return WineNews.builder()
                .title(wineNewsRequestDTO.getTitle())
                .cardNewsImg(wineNewsRequestDTO.getCardNewsImg())
                .build();
    }
}
