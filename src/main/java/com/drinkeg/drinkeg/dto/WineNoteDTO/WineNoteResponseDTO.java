package com.drinkeg.drinkeg.dto.WineNoteDTO;

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
public class WineNoteResponseDTO {

    private Long wineId;

    private float sugarContent;
    private float acidity;
    private float tannin;
    private float body;
    private float alcohol;

    @Builder.Default
    private List<String> scentAroma = new ArrayList<>();
    @Builder.Default
    private List<String> scentTaste = new ArrayList<>();
    @Builder.Default
    private List<String> scentFinish = new ArrayList<>();

}
