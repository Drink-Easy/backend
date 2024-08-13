package com.drinkeg.drinkeg.service.wineNewsBookMarkService;

import com.drinkeg.drinkeg.dto.WineNewsBookMarkDTO.request.WineNewsBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineNewsBookMarkDTO.response.WineNewsBookMarkResponseDTO;

import java.util.List;

public interface WineNewsBookMarkService {
    public WineNewsBookMarkResponseDTO saveWineNewsBookMark(Long userId, WineNewsBookMarkRequestDTO wineNewsBookMarkRequestDTO);
    public List<WineNewsBookMarkResponseDTO> getWineNewsBookMarksByUserId(Long userId);
    public void deleteWineNewsBookMarkById(Long wineNewsBookMarkId);
}
