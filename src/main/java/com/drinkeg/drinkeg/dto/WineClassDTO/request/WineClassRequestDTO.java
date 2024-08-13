package com.drinkeg.drinkeg.dto.WineClassDTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineClassRequestDTO {
    @NotNull(message = "제목은 필수입니다.")
    private String title;
    @NotNull(message = "영상은 필수입니다.")
    private String video;
    @NotNull(message = "설명은 필수입니다.")
    private String description;
}
