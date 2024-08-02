package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassBookMark;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.request.WineClassBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.response.WineClassBookMarkResponseDTO;

public class WineClassBookMarkConverter {
    public static WineClassBookMarkResponseDTO toWineClassBookMarkResponseDTO(WineClassBookMark wineClassBookMark) {
        return WineClassBookMarkResponseDTO.builder()
                .id(wineClassBookMark.getId())
                .userId(wineClassBookMark.getMember().getId())
                .wineClass(WineClassConverter.toWineClassResponseDTO(wineClassBookMark.getWineClass()))
                .build();
    }

    public static WineClassBookMark toWineClassBookMark(WineClass wineClass, Member member) {
        return WineClassBookMark.builder()
                .wineClass(wineClass)
                .member(member)
                .build();
    }
}
