package com.drinkeg.drinkeg.service.partyJoinMemberService;

public interface PartyJoinMemberService {
    void participateInParty(Long memberId, Long partyId);

    void cancelPartyJoin(Long memberId, Long partyId);
}
