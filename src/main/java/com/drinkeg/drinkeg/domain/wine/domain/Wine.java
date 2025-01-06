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

    private String name;

    private String imageUrl;

    private String sort; // 종류

    private String area; // 원산지

    private String variety; // 품종

    private float vivinoRating;

    private int price;

    @Embedded
    private WineNoteStatics wineNoteStatics;

    @OneToMany
    @JoinColumn(name = "wine_id")
    private final List<TastingNote> tastingNoteList = new ArrayList<>();

    public void updateImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    @Builder
    public Wine(String name, String imageUrl, String sort, String area, String variety, float vivinoRating, int price, WineNoteStatics wineNoteStatics) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.sort = sort;
        this.area = area;
        this.variety = variety;
        this.vivinoRating = vivinoRating;
        this.price = price;
        this.wineNoteStatics = wineNoteStatics;
    }
}


