package com.drinkeg.drinkeg.domain.member.domain;


import com.drinkeg.drinkeg.domain.member.converter.StringListConverter;
import com.drinkeg.drinkeg.domain.myWine.domain.MyWine;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String role;

    private String username;

    private String password;

    private String region;

    private Boolean isNewbie;

    // 처음 회원가입 한 사용자면 true이다가 회원 가입하면 false로 변함
    private Boolean isFirst;

    // 월 평균 와인 소비가의 범위중 최댓값
    private Long monthPriceMax;

    // 선호 종류, 품종, 국가
    @Convert(converter = StringListConverter.class)
    private List<String> wineSort = new ArrayList<>();

    @Convert(converter = StringListConverter.class)
    private List<String> wineArea = new ArrayList<>();

    private boolean agreement;


    // CascadeType.ALL: Member 엔티티가 삭제되면 연관된 TastingNote 엔티티도 삭제
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TastingNote> tastingNotes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WineWishlist> wineWishlists = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MyWine> myWines = new ArrayList<>();

    @Builder
    private Member(String name, String email, String role, String username, String password,
                   String region, Boolean isNewbie, Boolean isFirst, Long monthPriceMax,
                   List<String> wineSort, List<String> wineArea, boolean agreement) {
        this.name = name;
        this.email = email;
        this.role = role;

        this.username = username;
        this.password = password;

        this.region = region;
        this.isNewbie = isNewbie;
        this.isFirst = isFirst;
        this.monthPriceMax = monthPriceMax;
        this.wineSort = wineSort != null ? wineSort : new ArrayList<>();
        this.wineArea = wineArea != null ? wineArea : new ArrayList<>();
        this.agreement = agreement;
    }


    public void updateEmail(String email) { this.email = email; };

    public void updateFirstUser(String name, Boolean isNewbie, Long monthPrice,
                                List<String> wineSort, List<String> wineArea, String region){
        if(name != null) this.name = name;
        if(isNewbie != null) this.isNewbie = isNewbie;
        if(monthPrice != null) this.monthPriceMax = monthPrice;
        if(wineSort != null) this.wineSort = wineSort;
        if(wineArea != null) this.wineArea = wineArea;
        if(region != null) this.region = region;
        this.isFirst = false;
    }
}
