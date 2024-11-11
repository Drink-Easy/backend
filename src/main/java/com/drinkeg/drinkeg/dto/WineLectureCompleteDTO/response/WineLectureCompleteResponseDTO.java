package com.drinkeg.drinkeg.dto.WineLectureCompleteDTO.response;

import com.drinkeg.drinkeg.dto.WineLectureDTO.response.WineLectureResponseDTO;
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
