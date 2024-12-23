package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.party.domain.Party;
import com.drinkeg.drinkeg.domain.PartyBookmark;
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