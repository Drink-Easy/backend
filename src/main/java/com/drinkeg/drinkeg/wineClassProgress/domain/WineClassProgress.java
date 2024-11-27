package com.drinkeg.drinkeg.wineClassProgress.domain;

import com.drinkeg.drinkeg.domain.BaseEntity;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.wineClass.domain.WineClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class WineClassProgress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wine_class_id", nullable = false)
    private WineClass wineClass;

    private float progress;

    public void updateProgress(float progress) {
        this.progress = progress;
    }

    public static WineClassProgress create(WineClass wineClass, Member member) {
        return WineClassProgress.builder()
                .member(member)
                .wineClass(wineClass)
                .progress(0.0f)
                .build();
    }
}
