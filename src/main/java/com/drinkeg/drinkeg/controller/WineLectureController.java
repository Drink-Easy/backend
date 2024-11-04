package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineLectureDTO.request.WineLectureRequestDTO;
import com.drinkeg.drinkeg.dto.WineLectureDTO.response.WineLectureResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.wineLectureService.WineLectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wine Lecture", description = "와인 강의 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-lecture")
public class WineLectureController {
    private final WineLectureService wineLectureService;

    @Operation(
            summary = "와인 강의 전체 조회",
            description = "DB에 등록된 모든 와인 강의를 조회한다."
    )
    @GetMapping("")
    public ApiResponse<List<WineLectureResponseDTO>> showAllWineLectures(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WineLectureResponseDTO> wineLectureResponseDTOS = wineLectureService.showAllWineLectures(principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTOS);
    }

    @Operation(
            summary = "와인 클래스 기준 와인 강의 조회",
            description = "와인 클래스에 속한 모든 와인 강의를 조회한다."
    )
    @GetMapping("/wine-class/{wineClassId}")
    public ApiResponse<List<WineLectureResponseDTO>> showAllWineLecturesByWineClass(@PathVariable Long wineClassId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WineLectureResponseDTO> wineLectureResponseDTOS = wineLectureService.showAllWineLecturesByWineClass(wineClassId, principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTOS);
    }

    @Operation(
            summary = "와인 강의 단일 조회",
            description = "와인 강의 id로 와인 강의를 조회한다."
    )
    @GetMapping("/{wineLectureId}")
    public ApiResponse<WineLectureResponseDTO> showWineLectureById(@PathVariable Long wineLectureId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        WineLectureResponseDTO wineLectureResponseDTO = wineLectureService.showWineLectureById(wineLectureId, principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTO);
    }

    @Operation(
            summary = "와인 강의 저장",
            description = "와인 강의를 새롭게 저장한다. ADMIN 만 접근 가능하다."
    )
    @PostMapping("")
    public ApiResponse<WineLectureResponseDTO> saveWineLecture(@RequestBody WineLectureRequestDTO wineLectureRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        WineLectureResponseDTO wineLectureResponseDTO = wineLectureService.saveWineLecture(wineLectureRequestDTO, principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTO);
    }

    @Operation(
            summary = "와인 강의 수정",
            description = "와인 강의 id로 와인 강의를 수정한다. ADMIN 만 접근 가능하다."
    )
    @PutMapping("/{wineLectureId}")
    public ApiResponse<WineLectureResponseDTO> updateWineLecture(@PathVariable Long wineLectureId, @RequestBody WineLectureRequestDTO wineLectureRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        WineLectureResponseDTO wineLectureResponseDTO = wineLectureService.updateWineLecture(wineLectureRequestDTO, wineLectureId, principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTO);
    }

    @Operation(
            summary = "와인 강의 삭제",
            description = "와인 강의 id로 와인 강의를 삭제한다. ADMIN 만 접근 가능하다."
    )
    @DeleteMapping("{wineLectureId}")
    public ApiResponse<String> deleteWineLectureById(@PathVariable Long wineLectureId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        wineLectureService.deleteWineLecture(wineLectureId, principalDetail);
        return ApiResponse.onSuccess("와인 강의 삭제 성공");
    }

}
