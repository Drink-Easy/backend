package com.drinkeg.drinkeg.comment.domain;


import com.drinkeg.drinkeg.domain.BaseEntity;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.party.domain.Party;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder.Default
    private boolean isDeleted = false;
}
