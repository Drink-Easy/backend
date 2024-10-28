package com.drinkeg.drinkeg.dto.TastingNoteDTO.response;

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
public class NoteResponseDTO {

    private Long noteId;

    private Long wineId;
    private String wineName;
    private String sort;
    private String area;
    private String imageUrl;

    private String color;
    private LocalDate tasteDate;

    // 점수 0 ~ 5
    private int sugarContent;
    private int acidity;
    private int tannin;
    private int body;
    private int alcohol;

    @Builder.Default
    private List<String> scentAroma = new ArrayList<>();
    @Builder.Default
    private List<String> scentTaste = new ArrayList<>();
    @Builder.Default
    private List<String> scentFinish = new ArrayList<>();

    private float satisfaction;

    private String review;
}
