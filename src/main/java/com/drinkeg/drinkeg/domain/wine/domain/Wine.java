package com.drinkeg.drinkeg.domain.wine.domain;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.parameters.P;

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

    private String nameEng;

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

    public void updateWine(String name, String nameEng, Integer price, String sort, String country, String region, String variety, Float vivinoRating) {
        if(name != null) this.name = name;
        if(nameEng != null) this.nameEng = nameEng;
        if(price != null) this.price = price;
        if(sort != null) this.sort = sort;
        if(country != null) this.country = country;
        if(region != null) this.region = region;
        if(variety != null) this.variety = variety;
        if(vivinoRating != null) this.vivinoRating = vivinoRating;
    }

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

    public static Wine of(WineRegisterRequest wineRegisterRequest) {
        return Wine.builder()
                .name(wineRegisterRequest.getName())
                .nameEng(wineRegisterRequest.getNameEng())
                .sort(wineRegisterRequest.getSort())
                .country(wineRegisterRequest.getCountry())
                .region(wineRegisterRequest.getRegion())
                .variety(wineRegisterRequest.getVariety())
                .vivinoRating(wineRegisterRequest.getVivinoRating())
                .price(wineRegisterRequest.getPrice())
                .wineNoteStatistics(WineNoteStatistics.create())
                .build();
    }
}


