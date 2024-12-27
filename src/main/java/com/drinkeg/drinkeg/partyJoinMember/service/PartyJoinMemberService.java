package com.drinkeg.drinkeg.partyJoinMember.service;

import com.drinkeg.drinkeg.party.domain.Party;
import com.drinkeg.drinkeg.partyJoinMember.domain.PartyJoinMember;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

public interface PartyJoinMemberService {

    PartyJoinMember save(PartyJoinMember partyJoinMember);

    long countByParty(Party party);

    void participateInParty(PrincipalDetail principalDetail, Long partyId);

    void cancelPartyJoin(PrincipalDetail principalDetail, Long partyId);
}
