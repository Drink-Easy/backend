package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;


public interface TastingNoteService {

    public void saveTastingNote(NoteRequestDTO noteRequestDTO, PrincipalDetail principalDetail);

    public NoteResponseDTO showTastingNoteById(Long noteId, PrincipalDetail principalDetail);

    public AllNoteResponseDTO findAllTastingNote(PrincipalDetail principalDetail, String sort);

    public void updateTastingNote(Long noteId, NoteUpdateRequestDTO noteUpdateRequestDTO, PrincipalDetail principalDetail);

    public void deleteTastingNote(Long noteId, PrincipalDetail principalDetail);

}
