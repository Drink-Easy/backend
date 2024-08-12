package com.drinkeg.drinkeg.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String video;

    private String description;

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
