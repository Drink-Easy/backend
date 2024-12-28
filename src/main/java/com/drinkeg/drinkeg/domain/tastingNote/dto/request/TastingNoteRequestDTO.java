package com.drinkeg.drinkeg.domain.tastingNote.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TastingNoteRequestDTO {

    @NotNull(message = "와인 ID는 필수입니다.")
    private Long wineId;

    @NotBlank(message = "색상 선택 필수입니다.")
    private String color;

    @NotNull(message = "시음 날짜는 필수입니다")
    private LocalDate tasteDate;


    @NotNull(message = "당도 선택은 필수입니다.")
    @Min(value = 0, message = "당도는 0 이상 10 이하의 정수 값이어야 합니다.")
    @Max(value = 10, message = "당도는 0 이상 10 이하의 정수 값이어야 합니다.")
    private int sugarContent;

    @NotNull(message = "산도 선택은 필수입니다.")
    @Min(value = 0, message = "산도는 0 이상 10 이하의 정수 값이어야 합니다.")
    @Max(value = 10, message = "산도는 0 이상 10 이하의 정수 값이어야 합니다.")
    private int acidity;

    @NotNull(message = "탄닌 선택은 필수입니다.")
    @Min(value = 0, message = "탄닌은 0 이상 10 이하의 정수 값이어야 합니다.")
    @Max(value = 10, message = "탄닌은 0 이상 10 이하의 정수 값이어야 합니다.")
    private int tannin;

    @NotNull(message = "바디 선택은 필수입니다.")
    @Min(value = 0, message = "바디는 0 이상 10 이하의 정수 값이어야 합니다.")
    @Max(value = 10, message = "바디는 0 이상 10 이하의 정수 값이어야 합니다.")
    private int body;

    @NotNull(message = "알콜도 선택은 필수입니다.")
    @Min(value = 0, message = "알콜도는 0 이상 10 이하의 정수 값이어야 합니다.")
    @Max(value = 10, message = "알콜도는 0 이상 10 이하의 정수 값이어야 합니다.")
    private int alcohol;


    @Builder.Default
    @NotEmpty(message = "향 선택은 필수입니다.")
    private List<String> nose = new ArrayList<>();


    @NotNull(message = "만족도 선택은 필수입니다.")
    @Min(0)
    @Max(5)
    private float rating;

    private String review;

}
