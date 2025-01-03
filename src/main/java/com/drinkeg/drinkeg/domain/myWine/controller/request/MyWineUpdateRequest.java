package com.drinkeg.drinkeg.domain.myWine.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyWineUpdateRequest {

    private LocalDate purchaseDate;
    private Integer purchasePrice;
}
