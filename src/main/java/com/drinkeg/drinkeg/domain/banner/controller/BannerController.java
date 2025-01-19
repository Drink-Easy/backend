package com.drinkeg.drinkeg.domain.banner.controller;

import com.drinkeg.drinkeg.domain.banner.dto.response.AllBannerResponse;
import com.drinkeg.drinkeg.domain.banner.service.BannerService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Banner", description = "홈 화면 배너 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/banner")
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    @Operation(
            summary = "전체 배너 조회",
            description = "전체 배너를 조회합니다.<br>일반 유저의 홈 화면에서 사용합니다."
    )
    public ApiResponse<AllBannerResponse> showAllBanner() {
        AllBannerResponse allBannerResponse = bannerService.showAllBanner();
        return ApiResponse.onSuccess(allBannerResponse);
    }
}
