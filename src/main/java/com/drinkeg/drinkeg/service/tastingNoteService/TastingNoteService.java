package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePriviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;

import java.util.List;


public interface TastingNoteService {

    public void saveNote(NoteRequestDTO noteRequestDTO);

    public NoteResponseDTO showNoteById(Long noteId);

    public AllNoteResponseDTO findAllNoteByMember(Member member);

}
