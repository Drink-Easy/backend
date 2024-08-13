package com.drinkeg.drinkeg.service.partyService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyResponseDTO;

import java.util.List;

public interface PartyService {

    void createParty(PartyRequestDTO partyRequest, Member member);
    PartyResponseDTO getParty(Long id);
    List<PartyResponseDTO> getAllParties();
    PartyResponseDTO updateParty(Long id, PartyRequestDTO partyRequest, Long memberId );
    void deleteParty(Long id, Long memberId);
    //boolean isHost(Long partyId, Long memberId);
    void validatePartyRequest(PartyRequestDTO partyRequest);
}
