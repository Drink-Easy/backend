package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.controller.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.controller.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNotePreviewResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;

import java.util.List;


public interface TastingNoteService {

    Long saveTastingNote(TastingNoteRequest tastingNote, String username);

    TastingNoteResponse showTastingNoteByIdAndUsername(Long noteId, String username);

    AllTastingNoteResponse findAllTastingNote(TastingNoteWineSort wineSort, String username);

    void updateTastingNote(Long noteId, TastingNoteUpdateRequest tastingNoteUpdateRequest, String username);

    Long deleteTastingNote(Long noteId, String username);

    void setTastingNoteMemberNull(String username);

    List<TastingNotePreviewResponse> searchTastingNoteByWineName(String searchName, String username);

}