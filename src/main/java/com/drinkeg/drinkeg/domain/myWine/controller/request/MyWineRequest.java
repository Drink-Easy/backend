package com.drinkeg.drinkeg.domain.myWine.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWineRequest {

    @NotNull(message = "와인 id는 null 일 수 없습니다.")
    private Long wineId;

    @NotNull(message = "구매 날짜 입력은 필수입니다.")
    private LocalDate purchaseDate;

    private Integer purchasePrice;

    @Builder
    public MyWineRequest(Long wineId, LocalDate purchaseDate, int purchasePrice) {
        this.wineId = wineId;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
    }
}
