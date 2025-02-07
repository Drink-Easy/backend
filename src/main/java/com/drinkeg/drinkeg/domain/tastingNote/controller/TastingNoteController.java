package com.drinkeg.drinkeg.domain.tastingNote.controller;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNotePreviewResponse;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.tastingNote.controller.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.controller.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.tastingNote.service.TastingNoteService;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasting-note")
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;

    @PostMapping("/new-note")
    @Operation(summary = "새 노트 작성 요청", description = "TastingNoteRequestDTO를 가지고 새 노트 작성 요청")
    public ApiResponse<String> saveTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                               @RequestBody @Valid TastingNoteRequest tastingNoteRequest) {

        tastingNoteService.saveTastingNote(tastingNoteRequest, principalDetail.getUsername());

        return ApiResponse.onSuccess("테이스팅 노트 작성 완료");
    }

    @GetMapping("/all")
    @Operation(summary = "전체 테이스팅 노트 확인", description = "sort(전체, 레드, 화이트, 스파클링, 로제, 기타) 를 RequestParam 으로 조회")
    public ApiResponse<AllTastingNoteResponse> showAllTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                                  @RequestParam("sort") String sort,
                                                                  @ParameterObject @PageableDefault(size = 10) Pageable pageable) {

        AllTastingNoteResponse allTastingNote =
                tastingNoteService.findAllTastingNote(TastingNoteWineSort.of(sort), principalDetail.getUsername(), pageable);
        return ApiResponse.onSuccess(allTastingNote);
    }

    @GetMapping("/{noteId}")
    @Operation(summary = "선택 테이스팅 노트 열람", description = "선택한 테이스팅 노트의 noteId로 노트 열람")

    public ApiResponse<TastingNoteResponse> showTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                            @PathVariable("noteId") Long noteId) {

        TastingNoteResponse tastingNoteResponse =
                tastingNoteService.showTastingNoteByIdAndUsername(noteId, principalDetail.getUsername());
        return ApiResponse.onSuccess(tastingNoteResponse);
    }

    @PatchMapping("/{noteId}")
    @Operation(summary = "테이스팅 노트 수정", description = "선택한 테이스팅 노트의 noteId로 노트 수정") 
    public ApiResponse<String> updateTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                 @PathVariable("noteId") Long noteId,
                                                 @RequestBody @Valid TastingNoteUpdateRequest tastingNoteUpdateRequest) {

        tastingNoteService.updateTastingNote(noteId, tastingNoteUpdateRequest, principalDetail.getUsername());
        return ApiResponse.onSuccess("노트 수정 완료");
    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "선택 테이스팅 노트 삭제", description = "선택한 테이스팅 노트의 noteId로 노트 삭제")
    public ApiResponse<String> deleteTastingNote(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                 @PathVariable("noteId") Long noteId) {

        tastingNoteService.deleteTastingNote(noteId, principalDetail.getUsername());
        return ApiResponse.onSuccess("노트 삭제 완료");
    }

    @GetMapping
    @Operation(summary = "와인 이름으로 테이스팅 노트 검색", description = "와인 이름으로 테이스팅 노트 검색")
    public ApiResponse<PageResponse<TastingNotePreviewResponse>> searchTastingNoteByWineName(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                                                             @RequestParam("searchName") String searchName,
                                                                                             @ParameterObject @PageableDefault(size = 10) Pageable pageable) {

        PageResponse<TastingNotePreviewResponse> result = tastingNoteService.searchTastingNoteByWineName(searchName, principalDetail.getUsername(), pageable);
        return ApiResponse.onSuccess(result);
    }

}
