package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;

import java.util.List;
import java.util.Map;


public interface TastingNoteService {


    public void saveTastingNote(TastingNoteRequest tastingNote, String username);

    public TastingNoteResponse showTastingNoteById(Long noteId, String username);

    public AllTastingNoteResponse findAllTastingNote(String sort, String username);

    public void updateTastingNote(Long noteId, TastingNoteUpdateRequest tastingNoteUpdateRequest, String username);

    public void deleteTastingNote(Long noteId, String username);

    public List<Map<Long, String>> showMemberNoseMapList(String username);

}
