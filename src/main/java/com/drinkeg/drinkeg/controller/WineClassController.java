package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.wineClassService.WineClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wine Class", description = "와인 클래스 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-class")
public class WineClassController {

    private final WineClassService wineClassService;

    @Operation(
            summary = "와인 클래스 전체 조회",
            description = "DB에 등록된 모든 와인 클래스를 조회한다."
    )
    @GetMapping("")
    public ApiResponse<List<WineClassResponseDTO>> getAllWineClasses(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WineClassResponseDTO> wineClassResponseDTOS = wineClassService.showAllWineClasses(principalDetail);
        return ApiResponse.onSuccess(wineClassResponseDTOS);
    }

    @Operation(
            summary = "와인 클래스 단일 조회",
            description = "와인 클래스 id로 와인클래스를 조회한다."
    )
    @GetMapping("/{wineClassId}")
    public ApiResponse<WineClassResponseDTO> getWineClassById(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long wineClassId) {
        WineClassResponseDTO wineClassResponseDTO = wineClassService.showWineClassById(wineClassId, principalDetail);
        return ApiResponse.onSuccess(wineClassResponseDTO);
    }

    @Operation(
            summary = "와인 클래스 생성",
            description = "와인 클래스를 생성한다. ADMIN만 접근 가능하다."
    )
    @PostMapping("")
    public ApiResponse<String> createWineClass(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody @Valid WineClassRequestDTO wineClassRequestDTO) {
        wineClassService.saveWineClass(wineClassRequestDTO, principalDetail);
        return ApiResponse.onSuccess("와인클래스 생성 완료");
    }

    @Operation(
            summary = "와인 클래스 수정",
            description = "와인 클래스 id로 와인 클래스를 수정한다. ADMIN만 접근 가능하다."
    )
    @PutMapping("/{wineClassId}")
    public ApiResponse<String> updateWineClass(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long wineClassId, @RequestBody @Valid WineClassRequestDTO wineClassRequestDTO) {
        wineClassService.updateWineClass(wineClassId, wineClassRequestDTO, principalDetail);
        return ApiResponse.onSuccess("와인클래스 수정 완료");
    }

    @Operation(
            summary = "와인 클래스 삭제",
            description = "와인 클래스 id로 와인 클래스를 삭제한다. ADMIN만 접근 가능하다."
    )
    @DeleteMapping("/{wineClassId}")
    public ApiResponse<String> deleteWineClass(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long wineClassId) {
        wineClassService.deleteWineClass(wineClassId, principalDetail);
        return ApiResponse.onSuccess("와인클래스 삭제 완료");
    }
}
