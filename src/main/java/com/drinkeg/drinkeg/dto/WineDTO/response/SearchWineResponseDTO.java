package com.drinkeg.drinkeg.dto.WineDTO.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
public class SearchWineResponseDTO {

    private Long wineId;
    private String name;
    private String imageUrl;

    private String sort;
    private String area;

    private float satisfaction;

    private int price;

    private boolean isLiked;

    @QueryProjection // 생성자에 추가
    public SearchWineResponseDTO(Long wineId, String name, String imageUrl, String sort, String area,
                                 float satisfaction, int price, boolean isLiked) {
        this.wineId = wineId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.sort = sort;
        this.area = area;
        this.satisfaction = satisfaction;
        this.price = price;
        this.isLiked = isLiked;
    }

}
