package com.drinkeg.drinkeg.domain.partyBookmark.service;

import com.drinkeg.drinkeg.domain.party.dto.PartyResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface PartyBookmarkService {
    void createBookmark(PrincipalDetail principalDetail, Long partyId);

    void cancelBookmark(PrincipalDetail principalDetail, Long partyId);

    List<PartyResponseDTO> getMemberBookmarks(PrincipalDetail principalDetail);
}
