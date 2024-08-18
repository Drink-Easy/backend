package com.drinkeg.drinkeg.dto.HomeDTO;

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
public class HomeResponseDTO {

    private String name;

    private List<RecommendWineDTO> recommendWineDTOs = new ArrayList<>();


}
