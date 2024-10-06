package com.drinkeg.drinkeg.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @Builder.Default
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

    public WineClass updateCategory(String category) {
        this.category = category;
        return this;
    }

    public WineClass updateAuthor(Member author) {
        this.author = author;
        return this;
    }

}
