package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
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
    private String area;
    private String imageUrl;

    private String color;
    private LocalDate tasteDate;

    // 점수 0 ~ 5
    private int sugarContent;
    private int acidity;
    private int tannin;
    private int body;
    private int alcohol;

    private List<Map<Long, String>> noseMapList = new ArrayList<>();

    private float rating;

    private String review;

    @QueryProjection
    public TastingNoteResponse(Long noteId, Long wineId, String wineName, String sort,
                               String area, String imageUrl, String color, LocalDate tasteDate,
                               int sugarContent, int acidity, int tannin, int body, int alcohol,
                               List<TastingNoteNose> noseList, float rating, String review){
        this.noteId = noteId;
        this.wineId = wineId;
        this.wineName = wineName;
        this.sort = sort;
        this.area = area;
        this.imageUrl = imageUrl;
        this.color = color;
        this.tasteDate = tasteDate;
        this.sugarContent = sugarContent;
        this.acidity = acidity;
        this.tannin = tannin;
        this.body = body;
        this.alcohol = alcohol;
        this.noseMapList = noseList.stream()
                .map(nose -> Map.of(nose.getId(), nose.getNoseElement()))
                .collect(Collectors.toList());
        this.rating = rating;
        this.review = review;
    }

}
