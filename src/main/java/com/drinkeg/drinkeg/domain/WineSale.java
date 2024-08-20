package com.drinkeg.drinkeg.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wine_id")
    private Wine wine;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wine_store_id")
    private WineStore wineStore;

    private int price;

    public WineSale updateWineStore(WineStore wineStore) {
        this.wineStore = wineStore;
        return this;
    }

    public WineSale updatePrice(int price) {
        this.price = price;
        return this;
    }

    public WineSale updateWine(Wine wine) {
        this.wine = wine;
        return this;
    }
}
