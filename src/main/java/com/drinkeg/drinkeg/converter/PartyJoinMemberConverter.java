package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.PartyJoinMember;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PartyJoinMemberConverter {
    public PartyJoinMember toEntity(Member member, Party party, boolean isHost) {
        return PartyJoinMember.builder()
                .member(member)
                .party(party)
                .isHost(isHost)
                .build();
    }
}
