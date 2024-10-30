package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.TastingNoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.TastingNoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.TastingNoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;


public interface TastingNoteService {


    public void saveTastingNote(PrincipalDetail principalDetail, TastingNoteRequestDTO tastingNote);

    public TastingNoteResponseDTO showTastingNoteById(PrincipalDetail principalDetail, Long noteId);

    public AllTastingNoteResponseDTO findAllTastingNote(PrincipalDetail principalDetail, String sort);

    public void updateTastingNote(PrincipalDetail principalDetail, Long noteId, TastingNoteUpdateRequestDTO tastingNoteUpdateRequestDTO);

    public void deleteTastingNote(PrincipalDetail principalDetail, Long noteId);

}
