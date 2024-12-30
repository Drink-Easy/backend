package com.drinkeg.drinkeg.domain.tastingNote.controller;

import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequestDTO;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequestDTO;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponseDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.tastingNote.service.TastingNoteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasting-note")
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;

    // 새 노트 작성
    @PostMapping("/new-note")
    @Operation(summary = "새 노트 작성 요청", description = "TastingNoteRequestDTO를 가지고 새 노트 작성 요청")
    public ApiResponse<String> saveTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                               @RequestBody @Valid TastingNoteRequestDTO tastingNoteRequestDTO) {

        tastingNoteService.saveTastingNote(tastingNoteRequestDTO, principalDetail);

        return ApiResponse.onSuccess("노트 작성 완료");
    }

    // 전체 노트 보기
    @GetMapping("/all")
    @Operation(summary = "전체 테이스팅 노트 확인", description = "sort(red, white, sparkling, rose, all) 를 RequestParam 으로 조회")
    public ApiResponse<AllTastingNoteResponseDTO> showAllTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestParam("sort") String sort) {

        AllTastingNoteResponseDTO allTastingNote = tastingNoteService.findAllTastingNote(sort, principalDetail);
        return ApiResponse.onSuccess(allTastingNote);
    }

    // 선택한 노트 보기
    @GetMapping("/{noteId}")
    @Operation(summary = "선택 테이스팅 노트 열람", description = "선택한 테이스팅 노트의 noteId로 노트 열람")

    public ApiResponse<TastingNoteResponseDTO> showTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                               @PathVariable("noteId") Long noteId) {

        TastingNoteResponseDTO tastingNoteResponseDTO = tastingNoteService.showTastingNoteById(noteId, principalDetail);
        return ApiResponse.onSuccess(tastingNoteResponseDTO);
    }


    @PatchMapping("/{noteId}")
    @Operation(summary = "테이스팅 노트 수정", description = "선택한 테이스팅 노트의 noteId로 노트 수정") 
    public ApiResponse<String> updateTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                 @PathVariable("noteId") Long noteId,
                                                 @RequestBody @Valid TastingNoteUpdateRequestDTO tastingNoteUpdateRequestDTO) {

        tastingNoteService.updateTastingNote(noteId, tastingNoteUpdateRequestDTO, principalDetail);
        return ApiResponse.onSuccess("노트 수정 완료");
    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "선택 테이스팅 노트 삭제", description = "선택한 테이스팅 노트의 noteId로 노트 삭제")
    public ApiResponse<String> deleteTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                 @PathVariable("noteId") Long noteId) {
        tastingNoteService.deleteTastingNote(noteId, principalDetail);
        return ApiResponse.onSuccess("노트 삭제 완료");
    }

    // 선택한 노트 보기
    @GetMapping("/nose")
    @Operation(summary = "사용자가 선택/직접입력 한 Nose List 보여주기", description = "사용자가 선택/집접입력 한 Noes List 보여주기")

    public ApiResponse<List<Map<Long, String>>> showMemberNoseMapList(@AuthenticationPrincipal PrincipalDetail principalDetail) {

        List<Map<Long, String>> noseMapList = tastingNoteService.showMemberNoseMapList(principalDetail);
        return ApiResponse.onSuccess(noseMapList);
    }
}
