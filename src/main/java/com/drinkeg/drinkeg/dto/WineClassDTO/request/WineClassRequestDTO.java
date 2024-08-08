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
    @NotNull
    private String title;
    @NotNull
    private String video;
    @NotNull
    private String description;
}
