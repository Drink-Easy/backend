package com.drinkeg.drinkeg.domain.myWine.domain;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id", nullable = false)
    private Wine wine;

    private LocalDate purchaseDate;
    private Integer purchasePrice;

    @Builder
    public MyWine(Member member, Wine wine, LocalDate purchaseDate, int purchasePrice){
        this.member = member;
        this.wine = wine;

        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
    }

    public static MyWine create(Member member, Wine wine, LocalDate purchaseDate, int purchasePrice){
        return MyWine.builder()
                .member(member)
                .wine(wine)
                .purchaseDate(purchaseDate)
                .purchasePrice(purchasePrice)
                .build();
    }

    // 보유 와인 정보 수정
    public void update(LocalDate purchaseDate, Integer purchasePrice) {
        if(purchaseDate != null) this.purchaseDate = purchaseDate;
        if(purchasePrice != null) this.purchasePrice = purchasePrice;
    }
}
