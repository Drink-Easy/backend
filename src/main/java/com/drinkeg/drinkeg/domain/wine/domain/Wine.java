package com.drinkeg.drinkeg.domain.wine.domain;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String name;

    private String nameEng; // 추가

    private int price;

    private String sort; // 종류

    private String country; // 국가

    private String region; // 생산지

    private String variety; // 품종

    private float vivinoRating;

    @Embedded
    private WineNoteStatistics wineNoteStatistics;

    @OneToMany
    @JoinColumn(name = "wine_id")
    private final List<TastingNote> tastingNoteList = new ArrayList<>();

    public void updateImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    @Builder
    public Wine(String name, String nameEng, String imageUrl, String sort, String country, String region, String variety, float vivinoRating, int price, WineNoteStatistics wineNoteStatistics) {
        this.name = name;
        this.nameEng = nameEng;
        this.imageUrl = imageUrl;
        this.sort = sort;
        this.country = country;
        this.region = region;
        this.variety = variety;
        this.vivinoRating = vivinoRating;
        this.price = price;
        this.wineNoteStatistics = wineNoteStatistics != null ? wineNoteStatistics : WineNoteStatistics.create();
    }
}


