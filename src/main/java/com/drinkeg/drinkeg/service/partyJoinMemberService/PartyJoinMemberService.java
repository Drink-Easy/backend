package com.drinkeg.drinkeg.service.partyJoinMemberService;

import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.PartyJoinMember;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

public interface PartyJoinMemberService {

    PartyJoinMember save(PartyJoinMember partyJoinMember);

    long countByParty(Party party);

    void participateInParty(PrincipalDetail principalDetail, Long partyId);

    void cancelPartyJoin(PrincipalDetail principalDetail, Long partyId);
}
