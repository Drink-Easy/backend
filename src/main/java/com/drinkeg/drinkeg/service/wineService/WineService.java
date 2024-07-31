package com.drinkeg.drinkeg.service.wineService;

import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteWineRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteWineResponseDTO;

import java.util.List;

public interface WineService {

    public List<NoteWineResponseDTO> searchWinesByName(NoteWineRequestDTO noteWineRequestDTO);

    public Wine findWineById(Long wineId);

}
