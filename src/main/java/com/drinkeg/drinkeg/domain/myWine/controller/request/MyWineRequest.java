package com.drinkeg.drinkeg.domain.myWine.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyWineRequest {

    @NotNull(message = "와인 id는 null 일 수 없습니다.")
    private Long wineId;

    @NotNull(message = "구매 날짜 입력은 필수입니다.")
    private LocalDate purchaseDate;

    private int purchasePrice;
}
