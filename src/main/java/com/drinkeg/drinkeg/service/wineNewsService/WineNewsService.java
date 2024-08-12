package com.drinkeg.drinkeg.service.wineNewsService;

import com.drinkeg.drinkeg.dto.WineNewsDTO.request.WineNewsRequestDTO;
import com.drinkeg.drinkeg.dto.WineNewsDTO.response.WineNewsResponseDTO;

import java.util.List;

public interface WineNewsService {

    public List<WineNewsResponseDTO> getAllWineNewses();

    public WineNewsResponseDTO getWineNewsById(Long WineNewsId);

    public void saveWineNews(WineNewsRequestDTO wineNewsRequestDTO);

    public WineNewsResponseDTO updateWineNews(Long wineNewsId, WineNewsRequestDTO wineNewsRequestDTO);

    public void deleteWineNews(Long wineNewsId);
}
