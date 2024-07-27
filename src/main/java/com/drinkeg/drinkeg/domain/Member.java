package com.drinkeg.drinkeg.domain;


import com.drinkeg.drinkeg.converter.StringListConverter;
import jakarta.persistence.*;
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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String region;

    private Boolean isNewbie;

    // 월 평균 와인 소비가
    private Long monthPrice;

    // 선호 종류, 품종, 국가
    @Convert(converter = StringListConverter.class)
    private List<String> wineSort = new ArrayList<>();

    @Convert(converter = StringListConverter.class)
    private List<String> wineVariety = new ArrayList<>();

    @Convert(converter = StringListConverter.class)
    private List<String> wineNation = new ArrayList<>();

    private boolean agreement;

    private String name;

    private String email;

    // CascadeType.ALL: Member 엔티티가 삭제되면 연관된 TastingNote 엔티티도 삭제
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TastingNote> tastingNotes = new ArrayList<>();

    // CascadeType.ALL: Member 엔티티가 삭제되면 연관된 OrderInfo 엔티티도 삭제
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<OrderInfo> orderInfos = new ArrayList<>();

}
