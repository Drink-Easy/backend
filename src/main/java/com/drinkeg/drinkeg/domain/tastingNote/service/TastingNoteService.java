package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequestDTO;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequestDTO;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponseDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;
import java.util.Map;


public interface TastingNoteService {


    public void saveTastingNote(TastingNoteRequestDTO tastingNote, String username);

    public TastingNoteResponseDTO showTastingNoteById(Long noteId, String username);

    public AllTastingNoteResponseDTO findAllTastingNote(String sort, String username);

    public void updateTastingNote(Long noteId, TastingNoteUpdateRequestDTO tastingNoteUpdateRequestDTO, String username);

    public void deleteTastingNote(Long noteId, String username);

    public List<Map<Long, String>> showMemberNoseMapList(String username);

}
