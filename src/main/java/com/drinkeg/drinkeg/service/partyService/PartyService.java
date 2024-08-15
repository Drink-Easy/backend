package com.drinkeg.drinkeg.service.partyService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.PartyDTO.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyDTO.PartyResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PartyService {

    void createParty(PartyRequestDTO partyRequest, Member member);
    PartyResponseDTO getParty(Long id); // optional로 변경
    List<PartyResponseDTO> getAllParties();
    PartyResponseDTO updateParty(Long id, PartyRequestDTO partyRequest, Long memberId );
    void deleteParty(Long id, Long memberId);
    //boolean isHost(Long partyId, Long memberId);
    void validatePartyRequest(PartyRequestDTO partyRequest);
    Page<PartyResponseDTO> getSortedParties(String sortType, Pageable pageable);
}
