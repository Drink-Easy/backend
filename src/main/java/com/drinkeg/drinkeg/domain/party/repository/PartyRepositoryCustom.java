package com.drinkeg.drinkeg.party.repository;

import com.drinkeg.drinkeg.party.dto.PartyResponseDTO;

import java.util.List;

public interface PartyRepositoryCustom {
    List<PartyResponseDTO> findPartiesByMemberId(Long memberId);
}
