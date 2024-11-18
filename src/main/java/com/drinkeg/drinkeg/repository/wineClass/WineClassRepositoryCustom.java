package com.drinkeg.drinkeg.repository.wineClass;

import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;

import java.util.List;

public interface WineClassRepositoryCustom {
    public List<WineClassResponseDTO> findWineClassListByMemberId(Long memberId);
    public WineClassResponseDTO findWineClassByIdAndMemberId(Long id, Long memberId);
}
