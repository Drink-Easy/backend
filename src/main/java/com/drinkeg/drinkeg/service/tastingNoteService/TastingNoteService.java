package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePriviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;


public interface TastingNoteService {

    public void saveNote(NoteRequestDTO noteRequestDTO, PrincipalDetail principalDetail);

    public NoteResponseDTO showNoteById(Long noteId,PrincipalDetail principalDetail);

    public List<NotePriviewResponseDTO> findAllNote(PrincipalDetail principalDetail,String sort);

    public void updateTastingNote(Long noteId, NoteUpdateRequestDTO noteUpdateRequestDTO, PrincipalDetail principalDetail);

    public void deleteTastingNote(Long noteId, PrincipalDetail principalDetail);

}
