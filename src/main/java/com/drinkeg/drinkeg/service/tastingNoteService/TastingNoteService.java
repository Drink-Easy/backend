package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePriviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;


public interface TastingNoteService {

    public void saveNote(PrincipalDetail principalDetail, NoteRequestDTO noteRequestDTO);

    public NoteResponseDTO showNoteById(PrincipalDetail principalDetail, Long noteId);

    public List<NotePriviewResponseDTO> findAllNote(PrincipalDetail principalDetail);

    public void updateTastingNote(PrincipalDetail principalDetail, Long noteId, NoteUpdateRequestDTO noteUpdateRequestDTO);

    public void deleteTastingNote(PrincipalDetail principalDetail, Long noteId);

}
