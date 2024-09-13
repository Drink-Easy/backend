package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.wineClassService.WineClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-class")
public class WineClassController {

    private final WineClassService wineClassService;

    @GetMapping("")
    public ApiResponse<List<WineClassResponseDTO>> getAllWineClasses(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WineClassResponseDTO> wineClassResponseDTOS = wineClassService.getAllWineClasses(principalDetail);
        return ApiResponse.onSuccess(wineClassResponseDTOS);
    }

    @GetMapping("/{wineClassId}")
    public ApiResponse<WineClassResponseDTO> getWineClassById(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long wineClassId) {
        WineClassResponseDTO wineClassResponseDTO = wineClassService.getWineClassById(wineClassId, principalDetail);
        return ApiResponse.onSuccess(wineClassResponseDTO);
    }

    @PostMapping("")
    public ApiResponse<String> createWineClass(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody @Valid WineClassRequestDTO wineClassRequestDTO) {
        wineClassService.saveWineClass(wineClassRequestDTO, principalDetail);
        return ApiResponse.onSuccess("와인클래스 생성 완료");
    }

    @PutMapping("/{wineClassId}")
    public ApiResponse<WineClassResponseDTO> updateWineClass(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long wineClassId, @RequestBody @Valid WineClassRequestDTO wineClassRequestDTO) {
        WineClassResponseDTO wineClassResponseDTO = wineClassService.updateWineClass(wineClassId, wineClassRequestDTO, principalDetail);
        return ApiResponse.onSuccess(wineClassResponseDTO);
    }

    @DeleteMapping("/{wineClassId}")
    public ApiResponse<String> deleteWineClass(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long wineClassId) {
        wineClassService.deleteWineClass(wineClassId, principalDetail);
        return ApiResponse.onSuccess("와인클래스 삭제 완료");
    }
}
