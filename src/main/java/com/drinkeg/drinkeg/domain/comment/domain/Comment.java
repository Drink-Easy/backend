package com.drinkeg.drinkeg.domain.comment.domain;


import com.drinkeg.drinkeg.domain.BaseEntity;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.party.domain.Party;
import com.drinkeg.drinkeg.domain.recomment.domain.Recomment;
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
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String content;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recomment> recomments = new ArrayList<>();

    @Builder.Default
    private boolean isDeleted = false;

    public void addRecomment(Recomment recomment) {
        this.recomments.add(recomment);
        recomment.setParentComment(this); // 자식의 참조도 설정
    }

    public void removeRecomment(Recomment recomment) {
        this.recomments.remove(recomment);
        recomment.setParentComment(null); // 자식의 부모 참조 제거
    }
}
