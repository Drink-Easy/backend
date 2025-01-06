package com.drinkeg.drinkeg.domain.partyBookmark;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.party.domain.Party;
import com.drinkeg.drinkeg.domain.partyBookmark.domain.PartyBookmark;
import org.springframework.stereotype.Component;

@Component
public class PartyBookmarkConverter {

    public PartyBookmark toEntity(Member member, Party party) {
        return PartyBookmark.builder()
                .member(member)
                .party(party)
                .build();
    }
}