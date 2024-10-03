package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineLectureCompleteDTO.response.WineLectureCompleteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.WineLectureCompleteService.WineLectureCompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-lecture-complete")
public class WineLectureCompleteController {
    private final WineLectureCompleteService wineLectureCompleteService;

    @GetMapping("/")
    public ApiResponse<List<WineLectureCompleteResponseDTO>> getWineLectureCompleteByMember(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WineLectureCompleteResponseDTO> wineLectureCompleteResponseDTOS = wineLectureCompleteService.showWineLectureCompleteByMember(principalDetail);
        return ApiResponse.onSuccess(wineLectureCompleteResponseDTOS);
    }

    @PostMapping("/wine-lecture/{wineLectureId}")
    public ApiResponse<WineLectureCompleteResponseDTO> createWineLectureComplete(@PathVariable Long wineLectureId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        WineLectureCompleteResponseDTO wineLectureCompleteResponseDTO = wineLectureCompleteService.saveWineLectureComplete(wineLectureId, principalDetail);
        return ApiResponse.onSuccess(wineLectureCompleteResponseDTO);
    }

    @DeleteMapping("/{wineLectureCompleteId}")
    public ApiResponse<String> deleteWineLectureComplete(@PathVariable Long wineLectureCompleteId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        wineLectureCompleteService.deleteWineLectureCompleteById(wineLectureCompleteId, principalDetail);
        return ApiResponse.onSuccess("와인 강의 수강여부 삭제 완료");
    }
}
