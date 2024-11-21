package com.drinkeg.drinkeg.service.WineLectureCompleteService;


import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineLecture;
import com.drinkeg.drinkeg.dto.WineLectureCompleteDTO.response.WineLectureCompleteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface WineLectureCompleteService {
    WineLectureCompleteResponseDTO saveWineLectureComplete(Long wineClassId, PrincipalDetail principalDetail);
    void deleteWineLectureComplete(Long wineLectureId, PrincipalDetail principalDetail);

    boolean isCompleted(WineLecture wineLecture , Member member);
}
