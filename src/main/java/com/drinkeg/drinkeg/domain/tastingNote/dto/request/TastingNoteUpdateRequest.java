package com.drinkeg.drinkeg.domain.tastingNote.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TastingNoteUpdateRequest {

    private String color;
    private LocalDate tastingDate;

    @Min(value = 0, message = "당도는 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "당도는 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer sugarContent;

    @Min(value = 0, message = "산도는 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "산도는 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer acidity;

    @Min(value = 0, message = "탄닌은 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "탄닌은 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer tannin;

    @Min(value = 0, message = "바디는 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "바디는 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer body;

    @Min(value = 0, message = "알콜도는 0 이상 100 이하의 정수 값이어야 합니다.")
    @Max(value = 100, message = "알콜도는 0 이상 100 이하의 정수 값이어야 합니다.")
    private Integer alcohol;

    private List<String> updateNoseList;

    @Min(0)
    @Max(5)
    private Float rating;

    private String review;

    @Builder
    public TastingNoteUpdateRequest(String color, LocalDate tastingDate, Integer sugarContent, Integer acidity, Integer tannin, Integer body, Integer alcohol, List<String> updateNoseList, Float rating, String review) {
        this.color = color;
        this.tastingDate = tastingDate;
        this.sugarContent = sugarContent;
        this.acidity = acidity;
        this.tannin = tannin;
        this.body = body;
        this.alcohol = alcohol;
        this.updateNoseList = updateNoseList;
        this.rating = rating;
        this.review = review;
    }
}
