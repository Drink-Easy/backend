package com.drinkeg.drinkeg.domain;


import com.drinkeg.drinkeg.converter.StringListConverter;
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
    @Builder.Default
    @Convert(converter = StringListConverter.class)
    private List<String> wineSort = new ArrayList<>();

    @Builder.Default
    @Convert(converter = StringListConverter.class)
    private List<String> wineVariety = new ArrayList<>();

    @Builder.Default
    @Convert(converter = StringListConverter.class)
    private List<String> wineArea = new ArrayList<>();

    private boolean agreement;


    // CascadeType.ALL: Member 엔티티가 삭제되면 연관된 TastingNote 엔티티도 삭제
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TastingNote> tastingNotes = new ArrayList<>();

    public void updateEmail(String email) { this.email = email; };
    public void updateName(String name) { this.name = name; };
    public void updateIsNewbie(Boolean isNewbie) { this.isNewbie = isNewbie; };
    public void updateMonthPriceMax(Long monthPrice) { this.monthPriceMax = monthPrice; };
    public void updateWineSort(List<String> wineSort) { this.wineSort = wineSort; };
    public void updateWineVariety(List<String> wineVariety) { this.wineVariety = wineVariety; };
    public void updateWineNation(List<String> wineArea) { this.wineArea = wineArea; };
    public void updateRegion(String region) { this.region = region; };
    public void updateIsFirst(){ this.isFirst = false;};
}
