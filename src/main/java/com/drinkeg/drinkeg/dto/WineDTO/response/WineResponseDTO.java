package com.drinkeg.drinkeg.dto.WineDTO.response;

import com.drinkeg.drinkeg.domain.Nose;
import com.drinkeg.drinkeg.domain.Palate;
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
public class WineResponseDTO {

    private Long wineId;

    private String name;

    private String imageUrl;

    private int price;
    private String sort;
    private String area;

    private float sugarContent;
    private float acidity;
    private float tannin;
    private float body;
    private float alcohol;

    private Nose nose;
    private Palate palate;

    private float satisfaction;

}
