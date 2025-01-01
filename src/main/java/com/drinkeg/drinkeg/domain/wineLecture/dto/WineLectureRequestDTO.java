package com.drinkeg.drinkeg.domain.wineLecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineLectureRequestDTO {
    private Long id;
    private String title;
    private String content;
    private Long WineClassId;
}
