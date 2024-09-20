package com.drinkeg.drinkeg.service.WineLectureCompleteService;


import com.drinkeg.drinkeg.dto.WineLectureCompleteDTO.response.WineLectureCompleteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface WineLectureCompleteService {
    List<WineLectureCompleteResponseDTO> showWineLectureCompleteByMember(PrincipalDetail principalDetail);
    WineLectureCompleteResponseDTO saveWineLectureComplete(Long wineClassId, PrincipalDetail principalDetail);
    void deleteWineLectureCompleteById(Long wineLectureCompleteId, PrincipalDetail principalDetail);
}
