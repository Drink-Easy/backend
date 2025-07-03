package com.drinkeg.drinkeg.domain.wineVintage.domain;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class WineVintage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(updatable = false)
    private Integer vintageYear;

    @Embedded
    private WineNoteStatistics wineNoteStatistics;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wine_id", nullable = false)
    private Wine wine;

    @Builder
    private WineVintage(int vintageYear, Wine wine, WineNoteStatistics wineNoteStatistics) {
        this.vintageYear = vintageYear;
        this.wine = wine;
        this.wineNoteStatistics = wineNoteStatistics != null ? wineNoteStatistics : WineNoteStatistics.create();
    }

    public static WineVintage create(Integer vintageYear, Wine wine) {
        return WineVintage.builder()
                .vintageYear(vintageYear)
                .wine(wine)
                .wineNoteStatistics(WineNoteStatistics.create())
                .build();
    }

}
