package com.drinkeg.drinkeg.service.wineService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.dto.WineDTO.request.SearchWineRequestDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;

import java.util.List;

public interface WineService {

    public List<SearchWineResponseDTO> searchWinesByName(String searchName);

    public Wine findWineById(Long wineId);

    public List<RecommendWineDTO> recommendWine(Member member);

}
