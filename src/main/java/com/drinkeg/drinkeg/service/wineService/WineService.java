package com.drinkeg.drinkeg.service.wineService;

import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteWineRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteWineResponseDTO;

import java.util.List;

public interface WineService {

    public List<NoteWineResponseDTO> searchWinesByName(NoteWineRequestDTO noteWineRequestDTO);

}
