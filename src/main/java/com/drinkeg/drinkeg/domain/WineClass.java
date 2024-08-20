package com.drinkeg.drinkeg.domain;

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

    private String videoId;

    private String description;

    @OneToMany(mappedBy = "wineClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WineClassBookMark> bookmarks = new ArrayList<>();

    public WineClass updateTitle(String title) {
        this.title = title;
        return this;
    }
    public WineClass updateVideo(String video) {
        this.video = video;
        return this;
    }

    public WineClass updateDescription(String description) {
        this.description = description;
        return this;
    }
}
