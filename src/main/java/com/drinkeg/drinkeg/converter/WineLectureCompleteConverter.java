package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.wineLecture.domain.WineLecture;
import com.drinkeg.drinkeg.wineLectureComplete.domain.WineLectureComplete;
import com.drinkeg.drinkeg.wineLectureComplete.dto.WineLectureCompleteResponseDTO;

import java.time.LocalDateTime;

public class WineLectureCompleteConverter {
    public static WineLectureCompleteResponseDTO toWineLectureCompleteResponseDTO(WineLectureComplete wineLectureComplete) {
        return WineLectureCompleteResponseDTO.builder()
                .id(wineLectureComplete.getId())
                .wineLectureResponseDTO(WineLectureConverter.toWineLectureResponseDTO(wineLectureComplete.getWineLecture(), true))
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
