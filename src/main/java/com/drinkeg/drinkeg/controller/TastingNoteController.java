package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteWineRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteWineResponseDTO;
import com.drinkeg.drinkeg.service.tastingNoteService.TastingNoteService;
import com.drinkeg.drinkeg.service.wineService.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tastingNote")
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;
    private final WineService wineService;

    @PostMapping("/")
    public ApiResponse<String> saveNote(NoteRequestDTO noteRequestDTO) {

        tastingNoteService.saveNote(noteRequestDTO);
        return ApiResponse.onSuccess("노트 작성 완료");
    }
    @PostMapping("/wine")
    public ApiResponse<List<NoteWineResponseDTO>> saveNote(NoteWineRequestDTO noteWineRequestDTO) {

        List<NoteWineResponseDTO> noteWineResponseDTOS = wineService.searchWinesByName(noteWineRequestDTO);
        return ApiResponse.onSuccess(noteWineResponseDTOS);
    }
}
