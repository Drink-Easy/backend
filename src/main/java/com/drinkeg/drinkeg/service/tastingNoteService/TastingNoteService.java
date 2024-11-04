package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;


public interface TastingNoteService {


    public void saveNote(PrincipalDetail principalDetail, NoteRequestDTO noteRequestDTO);

    public NoteResponseDTO showNoteById(PrincipalDetail principalDetail, Long noteId);

    public AllNoteResponseDTO findAllTastingNote(PrincipalDetail principalDetail, String sort);

    public void updateTastingNote(PrincipalDetail principalDetail, Long noteId, NoteUpdateRequestDTO noteUpdateRequestDTO);

    public void deleteTastingNote(PrincipalDetail principalDetail, Long noteId);

}
