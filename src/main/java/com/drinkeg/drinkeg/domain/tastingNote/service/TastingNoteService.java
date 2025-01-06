package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;

import java.util.List;
import java.util.Map;


public interface TastingNoteService {

    void saveTastingNote(TastingNoteRequest tastingNote, String username);

    TastingNoteResponse showTastingNoteById(Long noteId, String username);

    AllTastingNoteResponse findAllTastingNote(String sort, String username);

    void updateTastingNote(Long noteId, TastingNoteUpdateRequest tastingNoteUpdateRequest, String username);

    void deleteTastingNote(Long noteId, String username);

    List<Map<Long, String>> showMemberNoseMapList(String username);

    public void setTastingNoteMemberNull(String username);

}