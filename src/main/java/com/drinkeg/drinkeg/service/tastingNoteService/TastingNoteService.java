package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePriviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;

import java.util.List;


public interface TastingNoteService {

    public void saveNote(NoteRequestDTO noteRequestDTO, Member member);

    public NoteResponseDTO showNoteById(Long noteId, Member member);

    public List<NotePriviewResponseDTO> findAllNoteByMember(Member member);

    public void updateTastingNote(Long noteId, NoteUpdateRequestDTO noteUpdateRequestDTO, Member member);

    public void deleteTastingNote(Long noteId, Member member);

}
