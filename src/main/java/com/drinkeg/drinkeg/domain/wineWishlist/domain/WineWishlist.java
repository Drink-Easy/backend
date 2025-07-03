package com.drinkeg.drinkeg.domain.wineWishlist.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
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
    @JoinColumn(name = "wine_vintage_id", nullable = false)
    private WineVintage wineVintage;

    @Builder
    private WineWishlist(Member member, WineVintage wineVintage){
        this.member = member;
        this.wineVintage = wineVintage;
    }

    public static WineWishlist create(Member member, WineVintage wineVintage) {
        return WineWishlist.builder()
                .member(member)
                .wineVintage(wineVintage)
                .build();
    }

}
