package com.drinkeg.drinkeg.domain.tastingNote.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TastingNoteNose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TastingNote와의 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasting_note_id", nullable = false)
    private TastingNote tastingNote;

    @Column(nullable = false)
    private String noseElement;

    public void updateTastingNote(TastingNote tastingNote) {
        this.tastingNote = tastingNote;
    }

    @Builder
    private TastingNoteNose(TastingNote tastingNote, String noseElement) {
        this.tastingNote = tastingNote;
        this.noseElement = noseElement;
    }

    public static TastingNoteNose create(TastingNote tastingNote, String noseElement) {
        return TastingNoteNose.builder()
                .tastingNote(tastingNote)
                .noseElement(noseElement).build();
    }
}