package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class WineInfoResponse {

    private Long wineId;

    private String name;
    private String nameEng;

    private String imageUrl;

    private int price;
    private String sort;
    private String country;
    private String region;
    private String variety;
    private float vivinoRating;

    private float avgSugarContent;
    private float avgAcidity;
    private float avgTannin;
    private float avgBody;
    private float avgAlcohol;

    private String nose1;
    private String nose2;
    private String nose3;

    private float avgMemberRating;

    private boolean liked;

    public static WineInfoResponse of(Wine wine, boolean isLiked) {
        return WineInfoResponse.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .nameEng(wine.getNameEng())
                .imageUrl(wine.getImageUrl())
                .price(wine.getPrice())
                .sort(wine.getSort())
                .country(wine.getCountry())
                .region(wine.getRegion())
                .variety(wine.getVariety())
                .vivinoRating(wine.getVivinoRating())
                .avgSugarContent(wine.getWineNoteStatistics().getAvgSugarContent())
                .avgAcidity(wine.getWineNoteStatistics().getAvgAcidity())
                .avgTannin(wine.getWineNoteStatistics().getAvgTannin())
                .avgBody(wine.getWineNoteStatistics().getAvgBody())
                .avgAlcohol(wine.getWineNoteStatistics().getAvgAlcohol())
                .avgMemberRating(wine.getWineNoteStatistics().getAvgMemberRating())
                .nose1(wine.getWineNoteStatistics().getNose1())
                .nose2(wine.getWineNoteStatistics().getNose2())
                .nose3(wine.getWineNoteStatistics().getNose3())
                .liked(isLiked).build();
    }

    @Builder
    public WineInfoResponse(
            Long wineId, String name, String nameEng, String imageUrl, int price, String sort, String country, String region,
            String variety, float vivinoRating, float avgSugarContent, float avgAcidity, float avgTannin, float avgBody,
            float avgAlcohol, String nose1, String nose2, String nose3, float avgMemberRating, boolean liked) {

        this.wineId = wineId;
        this.name = name;
        this.nameEng = nameEng;
        this.imageUrl = imageUrl;
        this.price = price;
        this.sort = sort;
        this.country = country;
        this.region = region;
        this.variety = variety;
        this.vivinoRating = vivinoRating;

        this.avgSugarContent = avgSugarContent;
        this.avgAcidity = avgAcidity;
        this.avgTannin = avgTannin;
        this.avgBody = avgBody;
        this.avgAlcohol = avgAlcohol;

        this.nose1 = nose1;
        this.nose2 = nose2;
        this.nose3 = nose3;
        this.avgMemberRating = avgMemberRating;

        this.liked = liked;
    }
}
