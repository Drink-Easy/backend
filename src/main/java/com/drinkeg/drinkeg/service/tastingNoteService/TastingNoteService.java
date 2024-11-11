package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.TastingNoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.TastingNoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.TastingNoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;


public interface TastingNoteService {


    public void saveTastingNote(TastingNoteRequestDTO tastingNote, PrincipalDetail principalDetail);

    public TastingNoteResponseDTO showTastingNoteById(Long noteId, PrincipalDetail principalDetail);

    public AllTastingNoteResponseDTO findAllTastingNote(String sort, PrincipalDetail principalDetail);

    public void updateTastingNote(Long noteId, TastingNoteUpdateRequestDTO tastingNoteUpdateRequestDTO, PrincipalDetail principalDetail);

    public void deleteTastingNote(Long noteId, PrincipalDetail principalDetail);

}
