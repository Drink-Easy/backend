package com.drinkeg.drinkeg.converter;


import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;

public class WineClassConverter {

    // WineCLass를 WineClassResponseDTO로 변환
    public static WineClassResponseDTO toWineClassResponseDTO(WineClass wineClass, boolean isLiked) {
        return WineClassResponseDTO.builder()
                .id(wineClass.getId())
                .title(wineClass.getTitle())
                .thumbnailUrl(wineClass.getThumbnailUrl())
                .content(wineClass.getCategory())
                .isLiked(isLiked)
                .build();
    }

    // WineClassResponseDTO를 WineCLass로 변환
    public static WineClass toWineClass(WineClassRequestDTO wineClassRequestDTO, Member author) {
        return WineClass.builder()
                .author(author)
                .title(wineClassRequestDTO.getTitle())
                .thumbnailUrl(wineClassRequestDTO.getThumbnailUrl())
                .content(wineClassRequestDTO.getContent())
                .build();
    }
}
