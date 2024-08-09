package com.drinkeg.drinkeg.service.wineClassBookMarkService;

import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.request.WineClassBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.response.WineClassBookMarkResponseDTO;

import java.util.List;


public interface WineClassBookMarkService {
    public WineClassBookMarkResponseDTO saveWineClassBookMark(Long userId, WineClassBookMarkRequestDTO wineClassBookMarkRequestDTO);
    public List<WineClassBookMarkResponseDTO> getWineClassBookMarksByUserId(Long userId);
    public void deleteWineClassBookMarkById(Long wineClassBookMarkId);
}
