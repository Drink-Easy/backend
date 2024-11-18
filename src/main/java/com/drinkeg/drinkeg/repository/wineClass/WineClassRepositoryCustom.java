package com.drinkeg.drinkeg.repository.wineClass;

import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;

import java.util.List;
import java.util.Optional;

public interface WineClassRepositoryCustom {
    public List<WineClassResponseDTO> findWineClassListByMemberId(Long memberId);
    public Optional<WineClassResponseDTO> findWineClassByIdAndMemberId(Long id, Long memberId);
}
