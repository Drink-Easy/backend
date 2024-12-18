package com.drinkeg.drinkeg.wineLectureComplete.dto;

import com.drinkeg.drinkeg.wineLecture.dto.WineLectureResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineLectureCompleteResponseDTO {
    private Long id;
    private WineLectureResponseDTO wineLectureResponseDTO;
}
