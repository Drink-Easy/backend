package com.drinkeg.drinkeg.domain.comment.domain;


import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.party.domain.Party;
import com.drinkeg.drinkeg.domain.recomment.domain.Recomment;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private List<Recomment> recomments;

    private boolean isDeleted = false;

    public static Comment create(Member member, Party party, String content) {
        return Comment.builder()
                .member(member)
                .party(party)
                .content(content)
                .build();
    }

    @Builder
    public Comment(Party party, Member member, String content, List<Recomment> recomments) {
        this.party = party;
        this.member = member;
        this.content = content;
        this.recomments = recomments != null ? recomments : new ArrayList<>();
        this.isDeleted = false;
    }

    public void addRecomment(Recomment recomment) {
        this.recomments.add(recomment);
        recomment.setParentComment(this); // 자식의 참조도 설정
    }

    public void removeRecomment(Recomment recomment) {
        this.recomments.remove(recomment);
        recomment.setParentComment(null); // 자식의 부모 참조 제거
    }
}
