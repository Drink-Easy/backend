package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePriviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.tastingNoteService.TastingNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasting-note")
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;

    // 새 노트 작성
    @PostMapping("/new-note")
    public ApiResponse<String> saveNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody @Valid NoteRequestDTO noteRequestDTO) {

        tastingNoteService.saveNote(noteRequestDTO, principalDetail);
        return ApiResponse.onSuccess("노트 작성 완료");
    }


    // 선택한 노트 보기
    @GetMapping("/{noteId}")
    public ApiResponse<NoteResponseDTO> showNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("noteId") Long noteId) {

        NoteResponseDTO noteResponseDTO = tastingNoteService.showNoteById(noteId, principalDetail);
        return ApiResponse.onSuccess(noteResponseDTO);
    }

    // 전체 노트 보기
    @GetMapping("/all-note")
    public ApiResponse<List<NotePriviewResponseDTO>> showAllNote(@AuthenticationPrincipal PrincipalDetail principalDetail) {

        List<NotePriviewResponseDTO> allNoteByMember = tastingNoteService.findAllNote(principalDetail);
        return ApiResponse.onSuccess(allNoteByMember);
    }

    @PatchMapping("/{noteId}")
    public ApiResponse<String> updateTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("noteId") Long noteId, @RequestBody @Valid NoteUpdateRequestDTO noteUpdateRequestDTO) {

        tastingNoteService.updateTastingNote(noteId, noteUpdateRequestDTO, principalDetail);
        return ApiResponse.onSuccess("노트 수정 완료");
    }

    @DeleteMapping("/{noteId}")
    public ApiResponse<String> deleteTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("noteId") Long noteId) {

        tastingNoteService.deleteTastingNote(noteId, principalDetail);
        return ApiResponse.onSuccess("노트 삭제 완료");
    }

}
