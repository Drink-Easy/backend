package com.drinkeg.drinkeg.domain.tastingNote.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.tastingNote.controller.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;
import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TastingNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wine_id", nullable = false)
    private Wine wine;

    private String color;

    private LocalDate tasteDate;

    private int sweetness;
    private int acidity;
    private int tannin;
    private int body;
    private int alcohol;

    @OneToMany(mappedBy = "tastingNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TastingNoteNose> noseList = new ArrayList<>();

    private float rating;

    @Column(length = 500)
    private String review;

    @Builder
    public TastingNote(Member member, Wine wine, String color, LocalDate tasteDate,
                       int sweetness, int acidity, int tannin, int body, int alcohol,
                       List<TastingNoteNose> noseList, float rating, String review) {
        this.member = member;
        this.wine = wine;
        this.color = color;
        this.tasteDate = tasteDate;
        this.sweetness = sweetness;
        this.acidity = acidity;
        this.tannin = tannin;
        this.body = body;
        this.alcohol = alcohol;
        this.noseList = noseList != null ? noseList : new ArrayList<>();
        this.rating = rating;
        this.review = review;
    }

    // TastingNote 생성 매서드
    public static TastingNote create(Member member, Wine wine, TastingNoteRequest tastingNoteRequest) {
        TastingNote tastingNote = TastingNote.builder()
                .member(member)
                .wine(wine)
                .color(tastingNoteRequest.getColor())
                .tasteDate(tastingNoteRequest.getTasteDate())
                .sweetness(tastingNoteRequest.getSweetness())
                .acidity(tastingNoteRequest.getAcidity())
                .tannin(tastingNoteRequest.getTannin())
                .body(tastingNoteRequest.getBody())
                .alcohol(tastingNoteRequest.getAlcohol())
                .rating(tastingNoteRequest.getRating())
                .review(tastingNoteRequest.getReview())
                .build();

        Set<String> uniqueNoseElements = new LinkedHashSet<>(tastingNoteRequest.getNose());
        uniqueNoseElements.forEach(tastingNote::addNoseElement);
        return tastingNote;
    }

    // 연관관계 편의 메소드
    public TastingNote addNoseElement(String noseElement) {
        TastingNoteNose nose = TastingNoteNose.create(this, noseElement);
        this.noseList.add(nose);
        return this;
    }

    public TastingNote removeTastingNoteNose(TastingNoteNose tastingNoteNose) {
        this.noseList.remove(tastingNoteNose);
        tastingNoteNose.updateTastingNote(null);
        return this;
    }

    public void updateTastingNote(String color, LocalDate tasteDate,
                                  Integer sweetness, Integer acidity, Integer tannin, Integer body, Integer alcohol,
                                  List<String> updateNoseList, Float rating, String review){
        if (color != null) this.color = color;
        if (tasteDate != null) this.tasteDate = tasteDate;
        if (sweetness != null) this.sweetness = sweetness;
        if (acidity != null) this.acidity = acidity;
        if (tannin != null) this.tannin = tannin;
        if (body != null) this.body = body;
        if (alcohol != null) this.alcohol = alcohol;
        if (updateNoseList != null) this.updateTastingNoteNoseList(updateNoseList);
        if (rating != null) this.rating = rating;
        if (review != null) this.review = review;
    }

    public void updateTastingNoteNoseList(List<String> updateNoseList) {

        this.noseList.removeIf(nose->{
            nose.updateTastingNote(null);
            return true;
        });

        Set<String> uniqueNoseElements = new LinkedHashSet<>(updateNoseList);
        uniqueNoseElements.forEach(this::addNoseElement);
    }
}
