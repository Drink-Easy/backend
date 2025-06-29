package com.drinkeg.drinkeg.domain.myWine.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWineUpdateRequest {

    private Integer vintageYear;
    private LocalDate purchaseDate;
    private Integer purchasePrice;

    @Builder
    public MyWineUpdateRequest(Integer vintageYear, LocalDate purchaseDate, Integer purchasePrice) {
        this.vintageYear = vintageYear;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
    }
}
