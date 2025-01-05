package com.drinkeg.drinkeg.domain.wine.domain;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wineNote.domain.WineNote;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    // 종류
    private String sort;

    // 원산지
    private String area;

    // 품종
    private String variety;

    private float vivinoRating;

    private int price;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_note_id")
    private WineNote wineNote;

    @OneToMany
    @JoinColumn(name = "wine_id")
    private final List<TastingNote> tastingNoteList = new ArrayList<>();

    public void updateImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    @Builder
    public Wine(String name, String imageUrl, String sort, String area, String variety, float vivinoRating, int price, WineNote wineNote) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.sort = sort;
        this.area = area;
        this.variety = variety;
        this.vivinoRating = vivinoRating;
        this.price = price;
        this.wineNote = wineNote;
    }
}
