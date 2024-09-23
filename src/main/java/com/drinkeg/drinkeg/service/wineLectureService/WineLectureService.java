package com.drinkeg.drinkeg.service.wineLectureService;

import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineLecture;
import com.drinkeg.drinkeg.dto.WineLectureDTO.request.WineLectureRequestDTO;
import com.drinkeg.drinkeg.dto.WineLectureDTO.response.WineLectureResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface WineLectureService {
    List<WineLectureResponseDTO> showAllWineLectures(PrincipalDetail principalDetail);
    List<WineLectureResponseDTO> showAllWineLecturesByWineClass(Long wineClassId, PrincipalDetail principalDetail);
    WineLectureResponseDTO showWineLectureById(Long wineLectureId,PrincipalDetail principalDetail);
    WineLectureResponseDTO saveWineLecture(WineLectureRequestDTO wineLectureRequestDTO, PrincipalDetail principalDetail);
    WineLectureResponseDTO updateWineLecture(WineLectureRequestDTO wineLectureRequestDTO, Long wineLectureId, PrincipalDetail principalDetail);
    void deleteWineLecture(Long wineLectureId, PrincipalDetail principalDetail);

    List<WineLecture> getAllWineLectures();
    List<WineLecture> getAllWineLecturesByWineClass(WineClass wineClass);
    WineLecture getWineLectureById(Long id);
}
