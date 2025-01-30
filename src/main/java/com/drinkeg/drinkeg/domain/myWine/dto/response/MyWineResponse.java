package com.drinkeg.drinkeg.domain.myWine.dto.response;

import com.drinkeg.drinkeg.domain.myWine.domain.MyWine;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWineResponse {
    private Long myWineId;
    private Long wineId;
    private String wineName;
    private String wineSort;
    private String wineCountry;
    private String wineRegion;
    private String wineVariety;
    private String wineImageUrl;
    private LocalDate purchaseDate;
    private int purchasePrice;
    private int period;

    public static MyWineResponse of(MyWine myWine, LocalDate currentDate) {

        Wine wine = myWine.getWine();
        LocalDate purchaseDate = myWine.getPurchaseDate();

        // 기간을 일수로 계산
        int period = (int) ChronoUnit.DAYS.between(purchaseDate, currentDate);

        return MyWineResponse.builder()
                .myWineId(myWine.getId())
                .wineId(wine.getId())
                .wineName(wine.getName())
                .wineSort(wine.getSort())
                .wineCountry(wine.getCountry())
                .wineRegion(wine.getRegion())
                .wineVariety(wine.getVariety())
                .wineImageUrl(wine.getImageUrl())
                .purchaseDate(purchaseDate)
                .purchasePrice(myWine.getPurchasePrice())
                .period(period)
                .build();
    }
}
