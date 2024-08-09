package com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineClassBookMarkRequestDTO {
    private Long wineClassId;
}
