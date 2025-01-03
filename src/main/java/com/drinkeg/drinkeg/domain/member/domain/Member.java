package com.drinkeg.drinkeg.domain.member.domain;


import com.drinkeg.drinkeg.domain.member.converter.StringListConverter;
import com.drinkeg.drinkeg.domain.member.enums.Provider;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String username;

    private String password;

    private String region;

    private Boolean isNewbie;

    @Builder.Default
    private boolean isAdult = false;

    // 처음 회원가입 한 사용자면 true이다가 회원 가입하면 false로 변함
    private Boolean isFirst;

    // 월 평균 와인 소비가의 범위중 최댓값
    private Long monthPriceMax;

    // 프로필 이미지
    private String imageUrl;

    // 선호 종류, 품종, 국가
    @Builder.Default
    @Convert(converter = StringListConverter.class)
    private List<String> wineSort = new ArrayList<>();

    @Builder.Default
    @Convert(converter = StringListConverter.class)
    private List<String> wineArea = new ArrayList<>();

    private boolean agreement;


    // CascadeType.ALL: Member 엔티티가 삭제되면 연관된 TastingNote 엔티티도 삭제
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TastingNote> tastingNotes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WineWishlist> wineWishlists = new ArrayList<>();


    public void updateEmail(String email) { this.email = email; };
    public void updateName(String name) { this.name = name; };
    public void updateIsNewbie(Boolean isNewbie) { this.isNewbie = isNewbie; };
    public void updateMonthPriceMax(Long monthPrice) { this.monthPriceMax = monthPrice; };
    public void updateWineSort(List<String> wineSort) { this.wineSort = wineSort; };
    public void updateWineNation(List<String> wineArea) { this.wineArea = wineArea; };
    public void updateRegion(String region) { this.region = region; };
    public void updateIsFirst(){ this.isFirst = false;};
    public void updateImageUrl(String imageUrl){this.imageUrl = imageUrl;}


    public static Member createMember(String username, String password, boolean isFirst) {
        return Member.builder()
                .username("drinkeg "+username)
                .password(password)
                .email(username)
                .provider(Provider.DRINKEG)
                .role(Role.USER)
                .isFirst(isFirst)
                .build();
    }

    public static Member createOAuthMember(String username, String email, String provider) {
        return Member.builder()
                .username(username)
                .email(email) // email 값을 claims에서 추출
                .role(Role.USER)
                .provider(Provider.fromValue(provider))
                .isFirst(true)
                .build();
    }

}
