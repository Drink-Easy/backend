package com.drinkeg.drinkeg.dto.TastingNoteDTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TastingNoteResponseDTO {

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
    private List<Map<Long, String>> noseList = new ArrayList<>();
    @Builder.Default
    private List<Map<Long, String>> palateList = new ArrayList<>();

    private float satisfaction;

    private String review;
}
