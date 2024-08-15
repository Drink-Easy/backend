package com.drinkeg.drinkeg.service.wineService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteWineRequestDTO;
import com.drinkeg.drinkeg.dto.WineDTO.SearchWineResponseDTO;

import java.util.List;

public interface WineService {

    public List<SearchWineResponseDTO> searchWinesByName(NoteWineRequestDTO noteWineRequestDTO);

    public Wine findWineById(Long wineId);

    public List<RecommendWineDTO> recommendWine(Member member);

}
