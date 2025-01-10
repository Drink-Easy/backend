package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import lombok.Builder;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TastingNoteResponse {

    private Long noteId;

    private Long wineId;
    private String wineName;
    private String sort;
    private String country;
    private String region;
    private String imageUrl;

    private String color;
    private LocalDate tasteDate;

    // 점수 0 ~ 5
    private int sugarContent;
    private int acidity;
    private int tannin;
    private int body;
    private int alcohol;

    private List<String> noseList = new ArrayList<>();

    private float rating;

    private String review;

    @Builder
    public TastingNoteResponse(Long noteId, Long wineId, String wineName, String sort,
                               String country, String region, String imageUrl, String color, LocalDate tasteDate,
                               int sugarContent, int acidity, int tannin, int body, int alcohol,
                               List<TastingNoteNose> noseList, float rating, String review){
        this.noteId = noteId;
        this.wineId = wineId;
        this.wineName = wineName;
        this.sort = sort;
        this.country = country;
        this.region = region;
        this.imageUrl = imageUrl;
        this.color = color;
        this.tasteDate = tasteDate;
        this.sugarContent = sugarContent;
        this.acidity = acidity;
        this.tannin = tannin;
        this.body = body;
        this.alcohol = alcohol;
        this.noseList = noseList.stream()
                .map(TastingNoteNose::getNoseElement)
                .collect(Collectors.toList());
        this.rating = rating;
        this.review = review;
    }

    public static TastingNoteResponse of(TastingNote tastingNote) {
        return TastingNoteResponse.builder()
                .noteId(tastingNote.getId())
                .wineId(tastingNote.getWine().getId())
                .wineName(tastingNote.getWine().getName())
                .sort(tastingNote.getWine().getSort())
                .country(tastingNote.getWine().getCountry())
                .region(tastingNote.getWine().getRegion())
                .imageUrl(tastingNote.getWine().getImageUrl())
                .color(tastingNote.getColor())
                .tasteDate(tastingNote.getTasteDate())
                .sugarContent(tastingNote.getSugarContent())
                .acidity(tastingNote.getAcidity())
                .tannin(tastingNote.getTannin())
                .body(tastingNote.getBody())
                .alcohol(tastingNote.getAlcohol())
                .noseList(tastingNote.getNoseList())
                .rating(tastingNote.getRating())
                .review(tastingNote.getReview())
                .build();
    }
}
