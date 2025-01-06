package com.drinkeg.drinkeg.domain.wineWishlist.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WineWishlist extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wine_id")
    private Wine wine;

    // WineWishlist 생성 매서드
    public static WineWishlist create(Member member, Wine wine) {
        return WineWishlist.builder()
                .member(member)
                .wine(wine)
                .build();
    }

}
