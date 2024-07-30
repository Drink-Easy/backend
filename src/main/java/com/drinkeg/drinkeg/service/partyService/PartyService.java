package com.drinkeg.drinkeg.service.partyService;

import com.drinkeg.drinkeg.dto.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyResponseDTO;

import java.util.List;

public interface PartyService {

    PartyResponseDTO createParty(PartyRequestDTO partyRequest);
    PartyResponseDTO getParty(Long id);
    List<PartyResponseDTO> getAllParties();
    PartyResponseDTO updateParty(Long id, PartyRequestDTO partyRequest);
    void deleteParty(Long id);
}
