package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineLectureCompleteDTO.response.WineLectureCompleteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.WineLectureCompleteService.WineLectureCompleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wine Lecture Complete", description = "와인 강의 수강 완료 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-lecture-complete")
public class WineLectureCompleteController {
    private final WineLectureCompleteService wineLectureCompleteService;

    @Operation(
            summary = "와인 강의 수강 완료 생성",
            description = "와인 강의 수강 완료를 생성한다."
    )
    @PostMapping("/wine-lecture/{wineLectureId}")
    public ApiResponse<WineLectureCompleteResponseDTO> createWineLectureComplete(@PathVariable Long wineLectureId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        WineLectureCompleteResponseDTO wineLectureCompleteResponseDTO = wineLectureCompleteService.saveWineLectureComplete(wineLectureId, principalDetail);
        return ApiResponse.onSuccess(wineLectureCompleteResponseDTO);
    }

    @Operation(
            summary = "와인 강의 수강 완료 삭제",
            description = "와인 강의 수강 완료를 삭제한다."
    )
    @DeleteMapping("/wine-lecture/{wineLectureId}")
    public ApiResponse<String> deleteWineLectureComplete(@PathVariable Long wineLectureId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        wineLectureCompleteService.deleteWineLectureComplete(wineLectureId, principalDetail);
        return ApiResponse.onSuccess("와인 강의 수강여부 삭제 완료");
    }
}
