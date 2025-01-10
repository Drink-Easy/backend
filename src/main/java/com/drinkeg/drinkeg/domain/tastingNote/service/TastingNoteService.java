package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;

import java.util.List;
import java.util.Map;


public interface TastingNoteService {

    Long saveTastingNote(TastingNoteRequest tastingNote, String username);

    TastingNoteResponse showTastingNoteByIdAndUsername(Long noteId, String username);

    AllTastingNoteResponse findAllTastingNote(TastingNoteWineSort wineSort, String username);

    void updateTastingNote(Long noteId, TastingNoteUpdateRequest tastingNoteUpdateRequest, String username);

    Long deleteTastingNote(Long noteId, String username);

    public void setTastingNoteMemberNull(String username);

}