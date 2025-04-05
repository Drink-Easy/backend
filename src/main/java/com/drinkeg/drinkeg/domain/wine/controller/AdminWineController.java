package com.drinkeg.drinkeg.domain.wine.controller;

import com.drinkeg.drinkeg.domain.wine.controller.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineUpdateRequest;
import com.drinkeg.drinkeg.domain.wine.dto.response.AdminWinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.service.AdminWineService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Wine Admin", description = "와인 관리자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/wine")
public class AdminWineController {
    private final AdminWineService adminWineService;


    @GetMapping
    @Operation(summary = "와인 검색", description = "와인 이름 또는 영어 이름으로 검색하여 와인의 기본 정보를 조회한다. " +
            "paging 기능의 sort는 디폴트 값(name)을 사용하는 것을 권장한다.")
    public ApiResponse<PageResponse<AdminWinePreviewResponse>> searchWine(
            @RequestParam(defaultValue = "") String searchName,
            @RequestParam(defaultValue = "") String wineSort,
            @RequestParam(defaultValue = "") String wineVariety,
            @RequestParam(defaultValue = "") String wineCountry,
            @ParameterObject @PageableDefault(size = 7, sort = "name") Pageable pageable) {

        PageResponse<AdminWinePreviewResponse> pageResponse = adminWineService
                .searchWinesAdmin(searchName, wineSort, wineVariety, wineCountry, pageable);

        return ApiResponse.onSuccess(pageResponse);
    }

    @GetMapping("/{wineId}")
    @Operation(
            summary = "와인 상세정보 조회",
            description = "와인의 상세정보를 반환한다."
    )
    public ApiResponse<?> getWine(@PathVariable Long wineId) {
        return ApiResponse.onSuccess(adminWineService.getWine(wineId));
    }

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

        if (wineUpdateRequest == null && wineImage == null) {
            throw new IllegalArgumentException("wineUpdateRequest 또는 wineImage 중 하나는 필수입니다.");
        }
        adminWineService.updateWine(wineId, wineUpdateRequest, wineImage);
        return ApiResponse.onSuccess("와인 수정 성공");
    }

}
