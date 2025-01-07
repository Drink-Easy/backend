package com.drinkeg.drinkeg.domain.myWine.dto.response;

import com.drinkeg.drinkeg.domain.myWine.domain.MyWine;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyWineResponse {
    private Long myWineid;

    private Long wineId;
    private String wineName;
    private String wineSort;
    private String wineArea;
    private String wineVariety;

    private LocalDate purchaseDate;
    private int purchasePrice;

    private int period;

    public static MyWineResponse of(MyWine myWine){

        Wine wine = myWine.getWine();
        LocalDate currentDate = LocalDate.now();
        LocalDate purchaseDate = myWine.getPurchaseDate();

        // 기간을 일수로 계산
        int period = (int) ChronoUnit.DAYS.between(purchaseDate, currentDate);

        return MyWineResponse.builder()
                .myWineid(myWine.getId())
                .wineId(wine.getId())
                .wineName(wine.getName())
                .wineSort(wine.getSort())
                .wineArea(wine.getCountry())
                .wineVariety(wine.getVariety())

                .purchaseDate(purchaseDate)
                .purchasePrice(myWine.getPurchasePrice())
                .period(period)
                .build();
    }
}
