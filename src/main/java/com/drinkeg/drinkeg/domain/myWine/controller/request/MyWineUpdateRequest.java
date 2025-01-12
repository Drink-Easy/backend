package com.drinkeg.drinkeg.domain.myWine.controller.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWineUpdateRequest {

    private LocalDate purchaseDate;
    private Integer purchasePrice;

    @Builder
    public MyWineUpdateRequest(LocalDate purchaseDate, Integer purchasePrice) {
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
    }
}
