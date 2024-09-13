package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.request.WineClassBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.response.WineClassBookMarkResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineClassBookMarkService.WineClassBookMarkService;
import com.drinkeg.drinkeg.service.wineClassService.WineClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("wine-class/bookmark")
public class WineClassBookMarkController {
    private final WineClassBookMarkService wineClassBookMarkService;

    @PostMapping("/")
    public ApiResponse<WineClassBookMarkResponseDTO> createWineClassBookMark(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody WineClassBookMarkRequestDTO wineClassBookMarkRequestDTO) {
        WineClassBookMarkResponseDTO wineClassBookMarkResponseDTO = wineClassBookMarkService.saveWineClassBookMark(principalDetail, wineClassBookMarkRequestDTO);
        return ApiResponse.onSuccess(wineClassBookMarkResponseDTO);
    }

    @GetMapping("/")
    public ApiResponse<List<WineClassBookMarkResponseDTO>> getWineClassBookMarkByUserId(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WineClassBookMarkResponseDTO> wineClassBookMarkResponseDTOS = wineClassBookMarkService.getWineClassBookMarksByUserId(principalDetail);
        return ApiResponse.onSuccess(wineClassBookMarkResponseDTOS);
    }

    @DeleteMapping("/{wineClassBookMarkId}")
    public ApiResponse<String> deleteWineClassBookMark(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long wineClassBookMarkId) {
        wineClassBookMarkService.deleteWineClassBookMarkById(principalDetail, wineClassBookMarkId);
        return ApiResponse.onSuccess("와인 클래스 북마크 삭제 완료.");
    }
}
