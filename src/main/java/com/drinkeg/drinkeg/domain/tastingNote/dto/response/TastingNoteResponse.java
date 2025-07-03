package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
import lombok.Builder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TastingNoteResponse {

    private Long noteId;
    private Long wineId;
    private String wineName;
    private Integer vintageYear;
    private String sort;
    private String country;
    private String region;
    private String variety;
    private String imageUrl;
    private String color;
    private LocalDate tasteDate;
    private int sweetness;
    private int acidity;
    private int tannin;
    private int body;
    private int alcohol;
    private List<String> noseList = new ArrayList<>();
    private float rating;
    private String review;
    private LocalDate createdAt;

    @Builder
    private TastingNoteResponse(Long noteId, Long wineId, String wineName, Integer vintageYear,
                                String sort, String country, String region, String variety,
                                String imageUrl, String color, LocalDate tasteDate,
                                int sweetness, int acidity, int tannin, int body, int alcohol,
                                List<TastingNoteNose> noseList, float rating, String review, LocalDate createdAt) {
        this.noteId = noteId;
        this.wineId = wineId;
        this.wineName = wineName;
        this.vintageYear = vintageYear;
        this.sort = sort;
        this.country = country;
        this.region = region;
        this.variety = variety;
        this.imageUrl = imageUrl;
        this.color = color;
        this.tasteDate = tasteDate;
        this.sweetness = sweetness;
        this.acidity = acidity;
        this.tannin = tannin;
        this.body = body;
        this.alcohol = alcohol;
        this.noseList = noseList.stream()
                .map(TastingNoteNose::getNoseElement)
                .collect(Collectors.toList());
        this.rating = rating;
        this.review = review;
        this.createdAt = createdAt;
    }

    public static TastingNoteResponse from(TastingNote tastingNote) {
        WineVintage wineVintage = tastingNote.getWineVintage();
        Wine wine = wineVintage.getWine();
        int vintageYear = wineVintage.getVintageYear();

        return TastingNoteResponse.builder()
                .noteId(tastingNote.getId())
                .wineId(wine.getId())
                .wineName(wine.getName())
                .vintageYear(vintageYear)
                .sort(wine.getSort())
                .country(wine.getCountry())
                .region(wine.getRegion())
                .variety(wine.getVariety())
                .imageUrl(wine.getImageUrl())
                .color(tastingNote.getColor())
                .tasteDate(tastingNote.getTasteDate())
                .sweetness(tastingNote.getSweetness())
                .acidity(tastingNote.getAcidity())
                .tannin(tastingNote.getTannin())
                .body(tastingNote.getBody())
                .alcohol(tastingNote.getAlcohol())
                .noseList(tastingNote.getNoseList())
                .rating(tastingNote.getRating())
                .review(tastingNote.getReview())
                .createdAt(LocalDate.from(tastingNote.getCreatedAt()))
                .build();
    }
}
