package com.drinkeg.drinkeg.service.wineClassService;

import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;

public interface WineClassService {
    // WineClass CRUD
    public List<WineClassResponseDTO> showAllWineClasses(PrincipalDetail principalDetail);
    public WineClassResponseDTO showWineClassById(Long wineClassId, PrincipalDetail principalDetail);
    public void saveWineClass(WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail);
    public WineClassResponseDTO updateWineClass(Long wineClassId, WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail);
    public void deleteWineClass(Long wineClassId, PrincipalDetail principalDetail);
}
