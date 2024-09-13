package com.drinkeg.drinkeg.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    private String title;

    private String content;

    private String thumbnailUrl;

    @OneToMany(mappedBy = "wineClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WineClassBookMark> bookmarks = new ArrayList<>();

    public WineClass updateTitle(String title) {
        this.title = title;
        return this;
    }
    public WineClass updateThumbnail(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public WineClass updateContent(String description) {
        this.content = content;
        return this;
    }
}
