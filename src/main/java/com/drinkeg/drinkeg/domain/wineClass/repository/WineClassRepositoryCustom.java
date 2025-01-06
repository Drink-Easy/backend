package com.drinkeg.drinkeg.domain.wineClass.repository;

import com.drinkeg.drinkeg.domain.wineClass.dto.WineClassResponseDTO;

import java.util.List;
import java.util.Optional;

public interface WineClassRepositoryCustom {
    public List<WineClassResponseDTO> findWineClassListByMemberId(Long memberId);
    public Optional<WineClassResponseDTO> findWineClassByIdAndMemberId(Long id, Long memberId);
}
