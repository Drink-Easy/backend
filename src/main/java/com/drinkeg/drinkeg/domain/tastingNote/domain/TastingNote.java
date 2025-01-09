package com.drinkeg.drinkeg.domain.tastingNote.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
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

        // Set으로 중복 제거
        Set<String> uniqueNoseElements = new HashSet<>(tastingNoteRequest.getNose());
        for (String noseElement : uniqueNoseElements) {
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

    public void updateTastingNote(String color, LocalDate tasteDate,
                                  Integer sugarContent, Integer acidity, Integer tannin, Integer body, Integer alcohol,
                                  List<String> updateNoseList, Float rating, String review){
        if(color != null) this.color = color;

        if(tasteDate != null) this.tasteDate = tasteDate;
        if(sugarContent != null) this.sugarContent = sugarContent;
        if(acidity != null) this.acidity = acidity;
        if(tannin != null) this.tannin = tannin;
        if(body != null) this.body = body;
        if(alcohol != null) this.alcohol = alcohol;
        if(updateNoseList != null) {
            this.updateTastingNoteNoseList(updateNoseList);
        }

        if(rating != null) this.rating = rating;
        if(review != null) this.review = review;
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

//    // nose 요소 추가 메서드
//    public void addNoseElement(String noseElement) {
//        this.noseList.add(new TastingNoteNose(this, noseElement));
//    }

    // todo : 업데이트 기능 리팩토링 필요
    public void updateTastingNoteNoseList(List<String> updateNoseList) {

        // updateNoseList를 Set으로 변환하여 빠르게 검색
        Set<String> uniqueNoseElements = new HashSet<>(updateNoseList);

        // 기존 noseList에서 updateNoseList에 없는 항목 삭제
        this.noseList.removeIf(nose -> !uniqueNoseElements.contains(nose.getNoseElement()));

        // updateNoseList에 있는 항목 추가
        for (String noseElement : uniqueNoseElements) {
            // noseList에서 해당 항목이 없는 경우에만 추가
            if (this.noseList.stream().noneMatch(nose -> nose.getNoseElement().equals(noseElement))) {
                this.addNoseElement(noseElement);
            }
        }
    }
}
