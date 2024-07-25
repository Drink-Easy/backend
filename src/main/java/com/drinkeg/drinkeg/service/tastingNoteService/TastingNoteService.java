package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteResponseDTO;


public interface TastingNoteService {

    public void saveNote(NoteRequestDTO noteRequestDTO);

}
