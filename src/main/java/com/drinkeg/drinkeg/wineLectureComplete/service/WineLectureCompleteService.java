package com.drinkeg.drinkeg.wineLectureComplete.service;


import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.wineLecture.domain.WineLecture;
import com.drinkeg.drinkeg.wineLectureComplete.dto.WineLectureCompleteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

public interface WineLectureCompleteService {
    WineLectureCompleteResponseDTO saveWineLectureComplete(Long wineClassId, PrincipalDetail principalDetail);
    void deleteWineLectureComplete(Long wineLectureId, PrincipalDetail principalDetail);

    boolean isCompleted(WineLecture wineLecture , Member member);
}
