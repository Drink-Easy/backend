package com.drinkeg.drinkeg.domain.wineLecture.service;

import com.drinkeg.drinkeg.domain.wineLecture.dto.WineLectureRequestDTO;
import com.drinkeg.drinkeg.domain.wineLecture.dto.WineLectureResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface WineLectureService {
    List<WineLectureResponseDTO> showAllWineLecturesByWineClass(Long wineClassId, PrincipalDetail principalDetail);
    WineLectureResponseDTO showWineLectureById(Long wineLectureId,PrincipalDetail principalDetail);
    void saveWineLecture(WineLectureRequestDTO wineLectureRequestDTO, PrincipalDetail principalDetail);
    void updateWineLecture(WineLectureRequestDTO wineLectureRequestDTO, Long wineLectureId, PrincipalDetail principalDetail);
    void deleteWineLecture(Long wineLectureId, PrincipalDetail principalDetail);
}
