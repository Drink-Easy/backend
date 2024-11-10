package com.drinkeg.drinkeg.service.partyBookmarkService;

import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

public interface PartyBookmarkService {
    void createBookmark(PrincipalDetail principalDetail, Long partyId);

    void cancelBookmark(PrincipalDetail principalDetail, Long partyId);
}
