package com.drinkeg.drinkeg.tastingNote.service;

import com.drinkeg.drinkeg.tastingNote.dto.request.TastingNoteRequestDTO;
import com.drinkeg.drinkeg.tastingNote.dto.request.TastingNoteUpdateRequestDTO;
import com.drinkeg.drinkeg.tastingNote.dto.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.tastingNote.dto.response.TastingNoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;


public interface TastingNoteService {


    public void saveTastingNote(TastingNoteRequestDTO tastingNote, PrincipalDetail principalDetail);

    public TastingNoteResponseDTO showTastingNoteById(Long noteId, PrincipalDetail principalDetail);

    public AllTastingNoteResponseDTO findAllTastingNote(String sort, PrincipalDetail principalDetail);

    public void updateTastingNote(Long noteId, TastingNoteUpdateRequestDTO tastingNoteUpdateRequestDTO, PrincipalDetail principalDetail);

    public void deleteTastingNote(Long noteId, PrincipalDetail principalDetail);

}
