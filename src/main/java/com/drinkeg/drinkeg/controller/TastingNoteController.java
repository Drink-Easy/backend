package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.TastingNoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.TastingNoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.TastingNoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.tastingNoteService.TastingNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasting-note")
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;

    // 새 노트 작성
    @PostMapping("/new-note")
    public ApiResponse<String> saveTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                               @RequestBody @Valid TastingNoteRequestDTO tastingNoteRequestDTO) {

        tastingNoteService.saveTastingNote(tastingNoteRequestDTO, principalDetail);
        return ApiResponse.onSuccess("노트 작성 완료");
    }

    // 전체 노트 보기
    @GetMapping("/all-note")
    public ApiResponse<AllTastingNoteResponseDTO> showAllTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam("sort") String sort) {

        AllTastingNoteResponseDTO allTastingNote = tastingNoteService.findAllTastingNote(sort, principalDetail);
        return ApiResponse.onSuccess(allTastingNote);
    }

    // 선택한 노트 보기
    @GetMapping("/{noteId}")
    public ApiResponse<TastingNoteResponseDTO> showTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                               @PathVariable("noteId") Long noteId) {

        TastingNoteResponseDTO tastingNoteResponseDTO = tastingNoteService.showTastingNoteById(noteId, principalDetail);
        return ApiResponse.onSuccess(tastingNoteResponseDTO);
    }


    @PatchMapping("/{noteId}")
    public ApiResponse<String> updateTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                 @PathVariable("noteId") Long noteId,
                                                 @RequestBody @Valid TastingNoteUpdateRequestDTO tastingNoteUpdateRequestDTO) {

        tastingNoteService.updateTastingNote(noteId, tastingNoteUpdateRequestDTO, principalDetail);
        return ApiResponse.onSuccess("노트 수정 완료");
    }

    @DeleteMapping("/{noteId}")
    public ApiResponse<String> deleteTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                 @PathVariable("noteId") Long noteId) {

        tastingNoteService.deleteTastingNote(noteId, principalDetail);
        return ApiResponse.onSuccess("노트 삭제 완료");
    }

}
