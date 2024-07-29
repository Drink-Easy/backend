package com.drinkeg.drinkeg.dto.TastingNoteDTO.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequestDTO {

    @NotNull
    private Long wineId;

    @NotNull
    private String color;

    @NotNull
    @Min(0)
    @Max(5)
    private int sugarContent;

    @NotNull
    @Min(0)
    @Max(5)
    private int acidity;

    @NotNull
    @Min(0)
    @Max(5)
    private int tannin;

    @NotNull
    @Min(0)
    @Max(5)
    private int body;

    @NotNull
    @Min(0)
    @Max(5)
    private int alcohol;

    @NotNull
    private List<String> scentAroma = new ArrayList<>();

    @NotNull
    private List<String> scentTaste = new ArrayList<>();

    @NotNull
    private List<String> scentFinish = new ArrayList<>();

    @NotNull
    @Min(0)
    @Max(5)
    private float satisfaction;

    private String memo;

}
