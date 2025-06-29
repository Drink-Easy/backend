package com.drinkeg.drinkeg.domain.myWine.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWineRequest {

    @NotNull(message = "와인 id는 null 일 수 없습니다.")
    private Long wineId;

    @Range(min = 1970, max = 2024, message = "빈티지는 1970 이상 2024 이하의 정수 값이어야 합니다.")
    private Integer vintageYear;

    @NotNull(message = "구매 날짜 입력은 필수입니다.")
    private LocalDate purchaseDate;

    @Min(value = 0, message = "구매 가격은 0 이상이어야 합니다.")
    @Max(value = 1000000000, message = "구매 가격은 1000000000 이하여야 합니다.")
    private Integer purchasePrice;

    @Builder
    public MyWineRequest(Long wineId, Integer vintageYear, LocalDate purchaseDate, int purchasePrice) {
        this.wineId = wineId;
        this.vintageYear = vintageYear;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
    }
}
