package com.drinkeg.drinkeg.domain.wineLecture.service;

import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;

public interface WineLectureCompleteService {
    void saveWineLectureComplete(Long wineClassId, PrincipalDetail principalDetail);
    void deleteWineLectureComplete(Long wineLectureId, PrincipalDetail principalDetail);
}
