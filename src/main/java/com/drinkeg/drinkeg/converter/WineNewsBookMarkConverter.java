package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineNews;
import com.drinkeg.drinkeg.domain.WineNewsBookMark;
import com.drinkeg.drinkeg.dto.WineNewsBookMarkDTO.response.WineNewsBookMarkResponseDTO;

public class WineNewsBookMarkConverter {
    public static WineNewsBookMarkResponseDTO toWineNewsBookMarkResponseDTO(WineNewsBookMark wineNewsBookMark) {
        return WineNewsBookMarkResponseDTO.builder()
                .id(wineNewsBookMark.getId())
                .userId(wineNewsBookMark.getMember().getId())
                .wineNews(WineNewsConverter.toWineNewsResponseDTO(wineNewsBookMark.getWineNews()))
                .build();
    }

    public static WineNewsBookMark toWineNewsBookMark(WineNews wineNews, Member member) {
        return WineNewsBookMark.builder()
                .wineNews(wineNews)
                .member(member)
                .build();
    }
}
