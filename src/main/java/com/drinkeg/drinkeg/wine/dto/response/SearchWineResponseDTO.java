package com.drinkeg.drinkeg.wine.dto.response;

import com.drinkeg.drinkeg.wine.domain.Wine;
import com.drinkeg.drinkeg.wineNote.domain.WineNote;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
public class SearchWineResponseDTO {

    private Long wineId;
    private String name;
    private String imageUrl;

    private String sort;
    private String area;

    private float vivinoRating;

    private int price;

    private boolean isLiked;

    @QueryProjection // 생성자에 추가
    public SearchWineResponseDTO(Long wineId, String name, String imageUrl, String sort, String area,
                                 float vivinoRating, int price, boolean isLiked) {
        this.wineId = wineId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.sort = sort;
        this.area = area;
        this.vivinoRating = vivinoRating;
        this.price = price;
        this.isLiked = isLiked;
    }

    public static SearchWineResponseDTO create(Wine wine, boolean isLiked) {
        return SearchWineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())

                .isLiked(isLiked)
                .sort(wine.getSort())
                .area(wine.getArea())

                .price(((wine.getPrice() * 1300) / 1000) * 1000)

                // wine 기본 평점과, 사용자 평점 중 높은거로
                .vivinoRating(Math.max(wine.getVivinoRating(),
                        Optional.ofNullable(wine.getWineNote()).map(WineNote::getAvgMemberRating).orElse((float) 0)))
                .build();
    }

}
