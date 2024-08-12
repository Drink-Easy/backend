package com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineClassBookMarkRequestDTO {
    @NotNull(message="와인 클래스 ID는 필수입니다.")
    private Long wineClassId;
}
