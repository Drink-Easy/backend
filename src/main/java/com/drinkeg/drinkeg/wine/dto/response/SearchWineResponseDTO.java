package com.drinkeg.drinkeg.wine.dto.response;

import com.drinkeg.drinkeg.wine.domain.Wine;
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
    private String variety;

    private float vivinoRating;

    private int price;

    private boolean isLiked;

    @QueryProjection // 생성자에 추가
    public SearchWineResponseDTO(Long wineId, String name, String imageUrl, String sort, String area, String variety,
                                 float vivinoRating, int price, boolean isLiked) {
        this.wineId = wineId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.sort = sort;
        this.area = area;
        this.variety = variety;
        this.vivinoRating = vivinoRating;
        this.price = price;
        this.isLiked = isLiked;
    }

    public static SearchWineResponseDTO create(Wine wine, boolean isLiked) {
        return SearchWineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())

                .sort(wine.getSort())
                .area(wine.getArea())
                .variety(wine.getVariety())

                .vivinoRating(wine.getVivinoRating())
                .price(((wine.getPrice() * 1300) / 1000) * 1000)
                .isLiked(isLiked)

                .build();
    }

}
