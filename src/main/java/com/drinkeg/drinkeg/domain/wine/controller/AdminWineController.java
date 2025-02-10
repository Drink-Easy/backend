package com.drinkeg.drinkeg.domain.wine.controller;

import com.drinkeg.drinkeg.domain.wine.controller.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineUpdateRequest;
import com.drinkeg.drinkeg.domain.wine.service.AdminWineService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Wine Admin", description = "와인 관리자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/wine")
public class AdminWineController {
    private final AdminWineService adminWineService;

    @PostMapping("")
    @Operation(
            summary = "와인 등록",
            description = "와인을 데이터베이스에 저장하기 위한 API"
    )
    public ApiResponse<?> saveWine(@RequestPart(value = "wineRegisterRequest") @Valid WineRegisterRequest wineRegisterRequest,
                                   @RequestPart(value = "wineImage", required = false) MultipartFile wineImage) {

        adminWineService.saveWine(wineRegisterRequest, wineImage);
        return ApiResponse.onSuccess("와인 등록 성공");
    }

    @PatchMapping("/{wineId}")
    @Operation(
            summary = "와인 정보 수정",
            description = "와인 정보를 수정하기 위한 API"
    )
    public ApiResponse<?> updateWine(@PathVariable Long wineId,
                                     @RequestPart(value = "wineUpdateRequest", required = false) @Valid WineUpdateRequest wineUpdateRequest,
                                     @RequestPart(value = "wineImage", required = false) MultipartFile wineImage) {

        if (wineUpdateRequest == null || wineImage == null) {
            throw new IllegalArgumentException("wineUpdateRequest 또는 wineImage 중 하나는 필수입니다.");
        }
        adminWineService.updateWine(wineId, wineUpdateRequest, wineImage);
        return ApiResponse.onSuccess("와인 수정 성공");
    }

}
