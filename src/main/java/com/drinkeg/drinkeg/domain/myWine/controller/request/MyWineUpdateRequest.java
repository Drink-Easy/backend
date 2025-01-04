package com.drinkeg.drinkeg.domain.myWine.controller.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWineUpdateRequest {

    private LocalDate purchaseDate;
    private Integer purchasePrice;
}
