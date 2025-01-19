package com.drinkeg.drinkeg.domain.tastingNote.controller.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TastingNoteRequest {

    @NotNull(message = "와인 ID는 필수입니다.")
    private Long wineId;

    @NotBlank(message = "색상 선택 필수입니다.")
    private String color;

    @NotNull(message = "시음 날짜는 필수입니다")
    private LocalDate tasteDate;

    @NotNull(message = "당도 선택은 필수입니다.")
    @Min(value = 0, message = "당도는 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "당도는 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer sweetness;

    @NotNull(message = "산도 선택은 필수입니다.")
    @Min(value = 0, message = "산도는 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "산도는 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer acidity;

    @NotNull(message = "탄닌 선택은 필수입니다.")
    @Min(value = 0, message = "탄닌은 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "탄닌은 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer tannin;

    @NotNull(message = "바디 선택은 필수입니다.")
    @Min(value = 0, message = "바디는 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "바디는 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer body;

    @NotNull(message = "알콜도 선택은 필수입니다.")
    @Min(value = 0, message = "알콜도는 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "알콜도는 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer alcohol;

    private List<String> nose = new ArrayList<>();

    @NotNull(message = "만족도 선택은 필수입니다.")
    @Min(value = 0, message = "만족도는 0 이상 5 이하의 실수 값이어야 합니다.")
    @Max(value = 5, message = "만족도는 0 이상 5 이하의 실수 값이어야 합니다.")
    private Float rating;

    private String review;

    @Builder
    public TastingNoteRequest(Long wineId, String color, LocalDate tasteDate,
                              Integer sweetness, Integer acidity, Integer tannin,
                              Integer body, Integer alcohol, List<String> nose, Float rating, String review) {
        this.wineId = wineId;
        this.color = color;
        this.tasteDate = tasteDate;
        this.sweetness = sweetness;
        this.acidity = acidity;
        this.tannin = tannin;
        this.body = body;
        this.alcohol = alcohol;
        this.nose = nose;
        this.rating = rating;
        this.review = review;
    }
}