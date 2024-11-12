package com.drinkeg.drinkeg.dto.TastingNoteDTO.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class TastingNoteUpdateRequestDTO {

    private String color;
    private LocalDate tastingDate;

    @Min(value = 0, message = "당도는 0 이상 5 이하의 정수 값이어야 합니다.")
    @Max(value = 5, message = "당도는 0 이상 5 이하의 정수 값이어야 합니다.")
    private Integer sugarContent;

    @Min(value = 0, message = "산도는 0 이상 5 이하의 정수 값이어야 합니다.")
    @Max(value = 5, message = "산도는 0 이상 5 이하의 정수 값이어야 합니다.")
    private Integer acidity;

    @Min(value = 0, message = "탄닌은 0 이상 5 이하의 정수 값이어야 합니다.")
    @Max(value = 5, message = "탄닌은 0 이상 5 이하의 정수 값이어야 합니다.")
    private Integer tannin;

    @Min(value = 0, message = "바디는 0 이상 5 이하의 정수 값이어야 합니다.")
    @Max(value = 5, message = "바디는 0 이상 5 이하의 정수 값이어야 합니다.")
    private Integer body;

    @Min(value = 0, message = "알콜도는 0 이상 5 이하의 정수 값이어야 합니다.")
    @Max(value = 5, message = "알콜도는 0 이상 5 이하의 정수 값이어야 합니다.")
    private Integer alcohol;

    private final List<String> addNoseList = new ArrayList<>();
    private final List<Long> removeNoseList = new ArrayList<>();
    private final List<String> addPalateList = new ArrayList<>();
    private final List<Long> removePalateList = new ArrayList<>();

    @Min(0)
    @Max(5)
    private Float satisfaction;

    private String review;
}
