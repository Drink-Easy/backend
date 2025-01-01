package com.drinkeg.drinkeg.domain.wineClass.domain;

import com.drinkeg.drinkeg.domain.wineLecture.domain.WineLecture;
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
public class WineClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    private String thumbnailUrl;

    @Builder.Default
    @OneToMany(mappedBy = "wineClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WineLecture> wineLectures = new ArrayList<>();

    public WineClass updateTitle(String title) {
        this.title = title;
        return this;
    }
    public WineClass updateThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public WineClass updateCategory(String category) {
        this.category = category;
        return this;
    }

    public static WineClass create(String title, String category, String thumbnailUrl) {
        return WineClass.builder()
                .title(title)
                .category(category)
                .thumbnailUrl(thumbnailUrl)
                .build();
    }
}
