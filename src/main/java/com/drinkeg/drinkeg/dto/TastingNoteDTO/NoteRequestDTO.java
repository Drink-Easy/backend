package com.drinkeg.drinkeg.dto.TastingNoteDTO;

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
    private int sugarContent;

    @NotNull
    private int acidity;

    @NotNull
    private int tannin;

    @NotNull
    private int body;

    @NotNull
    private int alcohol;

    @NotNull
    private List<String> scentAroma = new ArrayList<>();

    @NotNull
    private List<String> scentTaste = new ArrayList<>();

    @NotNull
    private List<String> scentFinish = new ArrayList<>();

    @NotNull
    private int satisfaction;

    private String memo;

}
