package com.drinkeg.drinkeg.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TastingNoteNose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TastingNote와의 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_note_id")
    private TastingNote tastingNote;

    // 향 요소
    private String noseElement;
}