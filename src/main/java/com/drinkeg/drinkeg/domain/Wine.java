package com.drinkeg.drinkeg.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    // 종 : 레드, 화이트 등등
    private String sort;

    // 지역
    private String area;

    // 품종 : 카베르네소비뇽, 샤도네이 등등
    private String variety;

    // wine.com 별점
    private float rating;

    // 가격
    private float price;

    // cascade = CascadeType.ALL : 와인이 저장될 때 같이 저장됨
    @OneToOne(mappedBy = "wine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private WineNote wineNote;

    @OneToMany
    @JoinColumn(name = "wine_id")
    private List<TastingNote> tastingNoteList = new ArrayList<>();

}
