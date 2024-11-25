package com.drinkeg.drinkeg.repository.wineLecture;

import com.drinkeg.drinkeg.dto.WineLectureDTO.response.WineLectureResponseDTO;

import java.util.List;
import java.util.Optional;

public interface WineLectureRepositoryCustom {
    List<WineLectureResponseDTO> findWineLectureListByWineClassIdAndMemberId(Long wineClassId, Long memberId);
    Optional<WineLectureResponseDTO> findWineLectureByIdAndMemberId(Long wineLectureId, Long memberId);
}
