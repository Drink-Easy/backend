package com.drinkeg.drinkeg.domain.wineClass.service;

import com.drinkeg.drinkeg.domain.wineClass.dto.WineClassRequestDTO;
import com.drinkeg.drinkeg.domain.wineClass.dto.WineClassResponseDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface WineClassService {
    // WineClass CRUD
    public List<WineClassResponseDTO> showAllWineClasses(PrincipalDetail principalDetail);
    public WineClassResponseDTO showWineClassById(Long wineClassId, PrincipalDetail principalDetail);
    public void saveWineClass(WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail);
    public void updateWineClass(Long wineClassId, WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail);
    public void deleteWineClass(Long wineClassId, PrincipalDetail principalDetail);
}
