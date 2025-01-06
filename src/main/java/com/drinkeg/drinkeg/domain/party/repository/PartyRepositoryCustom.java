package com.drinkeg.drinkeg.domain.party.repository;


import com.drinkeg.drinkeg.domain.party.dto.PartyResponseDTO;

import java.util.List;

public interface PartyRepositoryCustom {
    List<PartyResponseDTO> findPartiesByMemberId(Long memberId);
}
