package com.drinkeg.drinkeg.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineLecture {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wine_class_id", nullable = false)
    private WineClass wineClass;

    public WineLecture updateTitle(String title) {
        this.title = title;
        return this;
    }

    public WineLecture updateContent(String content) {
        this.content = content;
        return this;
    }

    public WineLecture updateWineClass(WineClass wineClass) {
        this.wineClass = wineClass;
        return this;
    }
}
