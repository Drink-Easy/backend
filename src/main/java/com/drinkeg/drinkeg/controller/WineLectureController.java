package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineLectureDTO.request.WineLectureRequestDTO;
import com.drinkeg.drinkeg.dto.WineLectureDTO.response.WineLectureResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.wineLectureService.WineLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-lecture")
public class WineLectureController {
    private final WineLectureService wineLectureService;

    @GetMapping("")
    public ApiResponse<List<WineLectureResponseDTO>> showAllWineLectures(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WineLectureResponseDTO> wineLectureResponseDTOS = wineLectureService.showAllWineLectures(principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTOS);
    }

    @GetMapping("/wine-class/{wineClassId}")
    public ApiResponse<List<WineLectureResponseDTO>> showAllWineLecturesByWineClass(@PathVariable Long wineClassId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WineLectureResponseDTO> wineLectureResponseDTOS = wineLectureService.showAllWineLecturesByWineClass(wineClassId, principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTOS);
    }

    @GetMapping("/{wineLectureId}")
    public ApiResponse<WineLectureResponseDTO> showWineLectureById(@PathVariable Long wineLectureId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        WineLectureResponseDTO wineLectureResponseDTO = wineLectureService.showWineLectureById(wineLectureId, principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTO);
    }

    @PostMapping("")
    public ApiResponse<WineLectureResponseDTO> saveWineLecture(@RequestBody WineLectureRequestDTO wineLectureRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        WineLectureResponseDTO wineLectureResponseDTO = wineLectureService.saveWineLecture(wineLectureRequestDTO, principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTO);
    }

    @PutMapping("/{wineLectureId}")
    public ApiResponse<WineLectureResponseDTO> updateWineLecture(@PathVariable Long wineLectureId, @RequestBody WineLectureRequestDTO wineLectureRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        WineLectureResponseDTO wineLectureResponseDTO = wineLectureService.updateWineLecture(wineLectureRequestDTO, wineLectureId, principalDetail);
        return ApiResponse.onSuccess(wineLectureResponseDTO);
    }

    @DeleteMapping("{wineLectureId}")
    public ApiResponse<String> deleteWineLectureById(@PathVariable Long wineLectureId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        wineLectureService.deleteWineLecture(wineLectureId, principalDetail);
        return ApiResponse.onSuccess("와인 강의 삭제 성공");
    }

}
