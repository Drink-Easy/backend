package com.drinkeg.drinkeg.domain.comment.domain;


import com.drinkeg.drinkeg.domain.model.BaseEntity;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.party.domain.Party;
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
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String content;


    private boolean isDeleted = false;

    public static Comment create(Member member, Party party, String content) {
        return Comment.builder()
                .member(member)
                .party(party)
                .content(content)
                .isDeleted(false)
                .build();
    }

    @Builder
    public Comment(Comment parent, Party party, Member member, String content, boolean isDeleted) {
        this.parent = parent;
        this.party = party;
        this.member = member;
        this.content = content;
        this.isDeleted = false;
    }


    public void addChild(Comment child) {
        this.children.add(child);
        child.parent = this;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
