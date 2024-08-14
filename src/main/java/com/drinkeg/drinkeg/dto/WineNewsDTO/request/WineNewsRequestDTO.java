package com.drinkeg.drinkeg.dto.WineNewsDTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineNewsRequestDTO {
    @NotNull(message = "제목은 필수입니다.")
    private String title;
    @NotNull(message = "카드뉴스 이미지는 필수입니다.")
    private String cardNewsImg;
}
