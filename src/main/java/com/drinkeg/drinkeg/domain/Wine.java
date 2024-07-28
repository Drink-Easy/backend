package com.drinkeg.drinkeg.domain;

import jakarta.persistence.*;
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

    private String name;

    private String picture;

    private String sort;

    private String nation;

    private String variety;

    private String vivinoRate;

    private String userRate;

    @OneToOne(mappedBy = "wine", fetch = FetchType.LAZY)
    private WineNote wineNote;

}
