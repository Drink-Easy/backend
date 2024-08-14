package com.drinkeg.drinkeg.service.wineClassService;

import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;

import java.util.List;

public interface WineClassService {

    public List<WineClassResponseDTO> getAllWineClasses();

    public WineClassResponseDTO getWineClassById(Long wineClassId);

    public void saveWineClass(WineClassRequestDTO wineClassRequestDTO);

    public WineClassResponseDTO updateWineClass(Long wineClassId, WineClassRequestDTO wineClassRequestDTO);

    public void deleteWineClass(Long wineClassId);

    public WineClass findWineClassById(Long wineClassId);
}
