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
@Builder
@AllArgsConstructor
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
        this.noseList = !noseList.isEmpty() ? noseList : new ArrayList<>();
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

                .sugarContent(tastingNoteRequest.getSugarContent())
                .acidity(tastingNoteRequest.getAcidity())
                .tannin(tastingNoteRequest.getTannin())
                .body(tastingNoteRequest.getBody())
                .alcohol(tastingNoteRequest.getAlcohol())

                .noseList(new ArrayList<>())
                .rating(tastingNoteRequest.getRating())
                .review(tastingNoteRequest.getReview())
                .build();

        for(String noseElement : tastingNoteRequest.getNose()){
            tastingNote.addNoseElement(noseElement);
        }

        return tastingNote;
    }


    public void updateTastingNote(String color, LocalDate tasteDate,
                                  Integer sugarContent, Integer acidity, Integer tannin, Integer body, Integer alcohol,
                                  List<String> addNoseList, Float rating, String review){
        if(color != null) this.color = color;
        if(tasteDate != null) this.tasteDate = tasteDate;

        if(sugarContent != null) this.sugarContent = sugarContent;
        if(acidity != null) this.acidity = acidity;
        if(tannin != null) this.tannin = tannin;
        if(body != null) this.body = body;
        if(alcohol != null) this.alcohol = alcohol;

        if(!addNoseList.isEmpty()) {
            addNoseList.forEach(addNoseElement ->
                    this.noseList.add(TastingNoteNose.builder()
                            .tastingNote(this)
                            .noseElement(addNoseElement)
                            .build())
            );
        }

        if(rating != null) this.rating = rating;
        if(review != null) this.review = review;
    }



    // nose 요소 추가 메서드
    public void addNoseElement(String noseElement) {
        TastingNoteNose nose = TastingNoteNose.builder()
                .tastingNote(this)
                .noseElement(noseElement)
                .build();
        this.noseList.add(nose);
    }

}
