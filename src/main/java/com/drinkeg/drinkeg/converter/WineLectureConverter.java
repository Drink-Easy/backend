package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.wineClass.domain.WineClass;
import com.drinkeg.drinkeg.wineLecture.domain.WineLecture;
import com.drinkeg.drinkeg.wineLecture.dto.WineLectureRequestDTO;
import com.drinkeg.drinkeg.wineLecture.dto.WineLectureResponseDTO;

public class WineLectureConverter {
    public static WineLectureResponseDTO toWineLectureResponseDTO(WineLecture wineLecture, boolean isCompleted) {
        return WineLectureResponseDTO.builder()
                .id(wineLecture.getId())
                .title(wineLecture.getTitle())
                .content(wineLecture.getContent())
                .isCompleted(isCompleted)
                .build();
    }

    public static WineLecture toWineLecture(WineLectureRequestDTO wineLectureRequestDTO, WineClass wineClass) {
        return WineLecture.builder()
                .wineClass(wineClass)
                .title(wineLectureRequestDTO.getTitle())
                .content(wineLectureRequestDTO.getContent())
                .build();

    }
}
