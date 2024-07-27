package com.drinkeg.drinkeg.dto.TastingNoteDTO.response;

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
public class NoteResponseDTO {

    private Long noteId;

    private Long wineId;

    private String name;

    private String picture;

    private String color;

    // 점수 1 ~ 5
    private int sugarContent;
    private int acidity;
    private int tannin;
    private int body;
    private int alcohol;

    private List<String> scentAroma = new ArrayList<>();

    private List<String> scentTaste = new ArrayList<>();

    private List<String> scentFinish = new ArrayList<>();

    private int satisfaction;

    private String memo;
}
