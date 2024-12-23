package com.drinkeg.drinkeg.partyBookmark.domain;

import com.drinkeg.drinkeg.domain.BaseEntity;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.party.domain.Party;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartyBookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;
}