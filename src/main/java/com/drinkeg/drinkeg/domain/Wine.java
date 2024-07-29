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

    // 엑셀에 있는 칼럼 추가 예시
//    private String wine_nm;
//    private String wine_area;
//    private String wine_ctg;
//    private String wine_prc;

    private String name;

    private String picture;

    private String sort;

    private String nation;

    private String variety;

    private String vivinoRate;

    private String userRate;

    // cascade = CascadeType.ALL : 와인이 저장될 때 같이 저장됨
    @OneToOne(mappedBy = "wine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private WineNote wineNote;

}
