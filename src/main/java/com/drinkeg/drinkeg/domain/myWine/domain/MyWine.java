package com.drinkeg.drinkeg.domain.myWine.domain;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyWine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id")
    private Wine wine;

    private LocalDate purchaseDate;
    private int purchasePrice;

    public static MyWine create(Member member, Wine wine, LocalDate purchaseDate, int purchasePrice){
        return MyWine.builder()
                .member(member)
                .wine(wine)
                .purchaseDate(purchaseDate)
                .purchasePrice(purchasePrice)
                .build();
    }

    // 구매 날짜 수정
    public void updatePurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    // 구매 가격 수정
    public void updatePurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
}
