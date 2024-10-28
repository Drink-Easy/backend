package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.tastingNoteService.TastingNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasting-note")
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;

    // 새 노트 작성
    @PostMapping("/new-note")
    public ApiResponse<String> saveNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody @Valid NoteRequestDTO noteRequestDTO) {

        tastingNoteService.saveNote(principalDetail, noteRequestDTO);
        return ApiResponse.onSuccess("노트 작성 완료");
    }


    // 선택한 노트 보기
    @GetMapping("/{noteId}")
    public ApiResponse<NoteResponseDTO> showNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("noteId") Long noteId) {

        NoteResponseDTO noteResponseDTO = tastingNoteService.showNoteById(principalDetail, noteId);
        return ApiResponse.onSuccess(noteResponseDTO);
    }

    // 전체 노트 보기
    @GetMapping("/{sort}")
    public ApiResponse<AllNoteResponseDTO> showAllNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("sort") String sort) {

        AllNoteResponseDTO allTastingNote = tastingNoteService.findAllTastingNote(principalDetail, sort);
        return ApiResponse.onSuccess(allTastingNote);
    }

    @PatchMapping("/{noteId}")
    public ApiResponse<String> updateTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("noteId") Long noteId, @RequestBody @Valid NoteUpdateRequestDTO noteUpdateRequestDTO) {

        tastingNoteService.updateTastingNote(principalDetail, noteId, noteUpdateRequestDTO);
        return ApiResponse.onSuccess("노트 수정 완료");
    }

    @DeleteMapping("/{noteId}")
    public ApiResponse<String> deleteTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("noteId") Long noteId) {

        tastingNoteService.deleteTastingNote(principalDetail, noteId);
        return ApiResponse.onSuccess("노트 삭제 완료");
    }

}
