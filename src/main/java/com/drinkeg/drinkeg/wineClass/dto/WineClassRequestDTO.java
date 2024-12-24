package com.drinkeg.drinkeg.wineClass.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineClassRequestDTO {
    @NotNull(message = "제목은 필수입니다.")
    private String title;
    @NotNull(message = "카테고리는 필수입니다.")
    private String category;
    @NotNull(message = "썸네일은 필수입니다.")
    private MultipartFile thumbnail;
}
