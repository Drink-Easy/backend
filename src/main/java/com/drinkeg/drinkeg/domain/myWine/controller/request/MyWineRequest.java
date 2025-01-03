package com.drinkeg.drinkeg.domain.myWine.controller.request;

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

    private Long wineId;
    private LocalDate purchaseDate;
    private int purchasePrice;
}
