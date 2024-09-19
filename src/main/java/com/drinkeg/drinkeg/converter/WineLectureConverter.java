package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineLecture;
import com.drinkeg.drinkeg.dto.WineLectureDTO.request.WineLectureRequestDTO;
import com.drinkeg.drinkeg.dto.WineLectureDTO.response.WineLectureResponseDTO;

public class WineLectureConverter {
    public static WineLectureResponseDTO toWineLectureResponseDTO(WineLecture wineLecture) {
        return WineLectureResponseDTO.builder()
                .id(wineLecture.getId())
                .title(wineLecture.getTitle())
                .content(wineLecture.getContent())
                .build();
    }

    public static WineLecture toWineLecture(WineLectureRequestDTO wineLectureRequestDTO, WineClass wineClass, Member author) {
        return WineLecture.builder()
                .wineClass(wineClass)
                .author(author)
                .title(wineLectureRequestDTO.getTitle())
                .content(wineLectureRequestDTO.getContent())
                .build();

    }
}
