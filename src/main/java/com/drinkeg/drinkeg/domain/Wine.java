package com.drinkeg.drinkeg.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    // 카테고리
    private String category;

    // 지역
    private String area;

    // 품종
    private String variety;

    // wine.com 별점
    private String rating;

    // 가격
    private int price;

    // cascade = CascadeType.ALL : 와인이 저장될 때 같이 저장됨
    @OneToOne(mappedBy = "wine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private WineNote wineNote;

}
