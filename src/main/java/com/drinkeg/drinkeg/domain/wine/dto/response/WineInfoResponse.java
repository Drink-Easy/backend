package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.dto.WineNoteStatisticsAvgDto;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class WineInfoResponse {

    private Long wineId;
    private String name;
    private String nameEng;
    private Integer vintageYear;
    private String imageUrl;
    private int price;
    private String sort;
    private String country;
    private String region;
    private String variety;
    private float vivinoRating;
    private float avgSweetness;
    private float avgAcidity;
    private float avgTannin;
    private float avgBody;
    private float avgAlcohol;
    private String nose1;
    private String nose2;
    private String nose3;
    private float avgMemberRating;
    private boolean liked;

    @Builder
    public WineInfoResponse(
            Long wineId, String name, String nameEng, Integer vintageYear, String imageUrl, int price, String sort, String country, String region,
            String variety, float vivinoRating, float avgSweetness, float avgAcidity, float avgTannin, float avgBody,
            float avgAlcohol, String nose1, String nose2, String nose3, float avgMemberRating, boolean liked) {

        this.wineId = wineId;
        this.name = name;
        this.nameEng = nameEng;
        this.vintageYear = vintageYear;
        this.imageUrl = imageUrl;
        this.price = price;
        this.sort = sort;
        this.country = country;
        this.region = region;
        this.variety = variety;
        this.vivinoRating = vivinoRating;

        this.avgSweetness = avgSweetness;
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

    public static WineInfoResponse of(WineVintage wineVintage, boolean isLiked) {
        Wine wine = wineVintage.getWine();
        Integer vintageYear = wineVintage.getVintageYear() == 0 ? null
                : wineVintage.getVintageYear();
        return WineInfoResponse.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .nameEng(wine.getNameEng())
                .vintageYear(vintageYear)
                .imageUrl(wine.getImageUrl())
                .price(wine.getPrice())
                .sort(wine.getSort())
                .country(wine.getCountry())
                .region(wine.getRegion())
                .variety(wine.getVariety())
                .vivinoRating(wine.getVivinoRating())
                .avgSweetness(wineVintage.getWineNoteStatistics().getAvgSweetness())
                .avgAcidity(wineVintage.getWineNoteStatistics().getAvgAcidity())
                .avgTannin(wineVintage.getWineNoteStatistics().getAvgTannin())
                .avgBody(wineVintage.getWineNoteStatistics().getAvgBody())
                .avgAlcohol(wineVintage.getWineNoteStatistics().getAvgAlcohol())
                .avgMemberRating(wineVintage.getWineNoteStatistics().getAvgMemberRating())
                .nose1(wineVintage.getWineNoteStatistics().getNose1())
                .nose2(wineVintage.getWineNoteStatistics().getNose2())
                .nose3(wineVintage.getWineNoteStatistics().getNose3())
                .liked(isLiked).build();
    }
}
