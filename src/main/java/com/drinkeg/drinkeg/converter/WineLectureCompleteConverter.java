package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineLecture;
import com.drinkeg.drinkeg.domain.WineLectureComplete;
import com.drinkeg.drinkeg.dto.WineLectureCompleteDTO.response.WineLectureCompleteResponseDTO;

import java.time.LocalDateTime;

public class WineLectureCompleteConverter {
    public static WineLectureCompleteResponseDTO toWineLectureCompleteResponseDTO(WineLectureComplete wineLectureComplete) {
        return WineLectureCompleteResponseDTO.builder()
                .id(wineLectureComplete.getId())
                .wineLectureResponseDTO(WineLectureConverter.toWineLectureResponseDTO(wineLectureComplete.getWineLecture()))
                .build();
    }

    public static WineLectureComplete toWineLectureComplete(WineLecture wineLecture, Member member) {
        return WineLectureComplete.builder()
                .wineLecture(wineLecture)
                .member(member)
                .completeDate(LocalDateTime.now())
                .build();
    }
}
