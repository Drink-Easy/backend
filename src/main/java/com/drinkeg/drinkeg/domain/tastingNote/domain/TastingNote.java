package com.drinkeg.drinkeg.domain.tastingNote.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "wine_id")
    private Wine wine;

    private String color;

    // 시음 날짜
    private LocalDate tasteDate;

    // 점수 0 ~ 5
    private int sugarContent;
    private int acidity;
    private int tannin;
    private int body;
    private int alcohol;

    // nose를 TastingNote와 OneToMany 관계로 설정
    @OneToMany(mappedBy = "tastingNote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TastingNoteNose> noseList = new ArrayList<>();

    // 만족도 0 ~ 5, 소수점 가능
    private float rating;

    private String review;

    public static TastingNote create(Member member, Wine wine, TastingNoteRequest tastingNoteRequest) {
        TastingNote tastingNote = TastingNote.builder()
                .member(member)
                .wine(wine)
                .color(tastingNoteRequest.getColor())
                .tasteDate(tastingNoteRequest.getTasteDate())
                .sugarContent(tastingNoteRequest.getSugarContent())
                .acidity(tastingNoteRequest.getAcidity())
                .tannin(tastingNoteRequest.getTannin())
                .body(tastingNoteRequest.getBody())
                .alcohol(tastingNoteRequest.getAlcohol())
                .rating(tastingNoteRequest.getRating())
                .review(tastingNoteRequest.getReview())
                .build();
        for (String noseElement : tastingNoteRequest.getNose()){
            tastingNote.addNoseElement(noseElement);
        }
        return tastingNote;
    }

    // 연관관계 편의 메소드
    public TastingNote addNoseElement(String noseElement) {
        TastingNoteNose nose = TastingNoteNose.create(this, noseElement);
        this.noseList.add(nose);
        return this;
    }

    public TastingNote removeTastingNoteNose(TastingNoteNose tastingNoteNose) {
        noseList.remove(tastingNoteNose);
        tastingNoteNose.updateTastingNote(null);
        return this;
    }

    public void updateColor(String color) {
        this.color = color;
    }
    public void updateTasteDate(LocalDate tasteDate) {
        this.tasteDate = tasteDate;
    }
    public void updateSugarContent(int sugarContent) {
        this.sugarContent = sugarContent;
    }
    public void updateAcidity(int acidity) {
        this.acidity = acidity;
    }
    public void updateTannin(int tannin) {
        this.tannin = tannin;
    }
    public void updateBody(int body) {
        this.body = body;
    }
    public void updateAlcohol(int alcohol) {
        this.alcohol = alcohol;
    }
    public void updateRating(float rating) {
        this.rating = rating;
    }
    public void updateReview(String review) {
        this.review = review;
    }

    @Builder
    public TastingNote(Member member, Wine wine, String color, LocalDate tasteDate,
                       int sugarContent, int acidity, int tannin, int body, int alcohol,
                       List<TastingNoteNose> noseList, float rating, String review) {
        this.member = member;
        this.wine = wine;
        this.color = color;
        this.tasteDate = tasteDate;
        this.sugarContent = sugarContent;
        this.acidity = acidity;
        this.tannin = tannin;
        this.body = body;
        this.alcohol = alcohol;
        this.noseList = noseList != null ? noseList : new ArrayList<>();
        this.rating = rating;
        this.review = review;
    }
}
