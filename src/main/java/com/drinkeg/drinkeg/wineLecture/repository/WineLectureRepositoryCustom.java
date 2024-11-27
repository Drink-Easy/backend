package com.drinkeg.drinkeg.wineLecture.repository;

import com.drinkeg.drinkeg.wineLecture.dto.WineLectureResponseDTO;

import java.util.List;
import java.util.Optional;

public interface WineLectureRepositoryCustom {
    List<WineLectureResponseDTO> findWineLectureListByWineClassIdAndMemberId(Long wineClassId, Long memberId);
    Optional<WineLectureResponseDTO> findWineLectureByIdAndMemberId(Long wineLectureId, Long memberId);
}
