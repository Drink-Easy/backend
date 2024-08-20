package com.drinkeg.drinkeg.converter;


import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class WineClassConverter {

    // WineCLass를 WineClassResponseDTO로 변환
    public static WineClassResponseDTO toWineClassResponseDTO(WineClass wineClass) {
        return WineClassResponseDTO.builder()
                .id(wineClass.getId())
                .title(wineClass.getTitle())
                .video(wineClass.getVideoId())
                .description(wineClass.getDescription())
                .build();
    }

    // WineClassResponseDTO를 WineCLass로 변환
    public static WineClass toWineClass(WineClassRequestDTO wineClassRequestDTO) {
        return WineClass.builder()
                .title(wineClassRequestDTO.getTitle())
                .videoId(wineClassRequestDTO.getVideoId())
                .description(wineClassRequestDTO.getDescription())
                .build();
    }
}
