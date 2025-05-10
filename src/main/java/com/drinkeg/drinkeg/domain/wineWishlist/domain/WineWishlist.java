package com.drinkeg.drinkeg.domain.wineWishlist.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineWishlist extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wine_id", nullable = false)
    private Wine wine;

    @Builder
    private WineWishlist(Member member, Wine wine){
        this.member = member;
        this.wine = wine;
    }

    public static WineWishlist create(Member member, Wine wine) {
        return WineWishlist.builder()
                .member(member)
                .wine(wine)
                .build();
    }

}
