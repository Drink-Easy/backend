package com.drinkeg.drinkeg.service.partyService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.PartyJoinMember;
import com.drinkeg.drinkeg.dto.PartyDTO.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyDTO.PartyResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PartyService {

    Party findPartyById(Long partyId);
    Party saveParty(Party party);
    void createParty(PartyRequestDTO partyRequest, PrincipalDetail principalDetail);
    PartyResponseDTO getParty(Long id); // optional로 변경
    List<PartyResponseDTO> getAllParties();
    PartyResponseDTO updateParty(Long id, PartyRequestDTO partyRequest, PrincipalDetail principalDetail );
    void deleteParty(Long id, PrincipalDetail principalDetail);
    //boolean isHost(Long partyId, Long memberId);
    void validatePartyRequest(PartyRequestDTO partyRequest);
    Page<PartyResponseDTO> getSortedParties(String sortType, PrincipalDetail principalDetail, Pageable pageable);
    List<PartyResponseDTO> searchPartiesByName(String searchName);
}
