package com.drinkeg.drinkeg.domain.tastingNote.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"tasting_note_id", "nose_element"})
)
public class TastingNoteNose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TastingNote와의 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_note_id", nullable = false)
    private TastingNote tastingNote;

    // 향 요소
    @Column(nullable = false)
    private String noseElement;

    @Builder
    public TastingNoteNose(TastingNote tastingNote, String noseElement){
        this.tastingNote = tastingNote;
        this.noseElement = noseElement;
    }
}