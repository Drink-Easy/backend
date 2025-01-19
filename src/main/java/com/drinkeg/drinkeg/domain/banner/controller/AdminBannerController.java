package com.drinkeg.drinkeg.domain.banner.controller;

import com.drinkeg.drinkeg.domain.banner.dto.request.BannerCreateRequest;
import com.drinkeg.drinkeg.domain.banner.dto.response.BannerResponse;
import com.drinkeg.drinkeg.domain.banner.service.BannerService;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Admin Banner", description = "관리자 홈 화면 배너 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/banner")
public class AdminBannerController {

    private final BannerService bannerService;

    @PostMapping()
    @Operation(
            summary = "홈 화면 배너 생성",
            description = "홈 화면 배너를 생성합니다.<br>ADMIN만 접근 가능합니다."
    )
    public ApiResponse<String> saveBanner(@RequestPart(value = "bannerImage") MultipartFile bannerImage,
                                          @RequestPart(value = "banner") BannerCreateRequest bannerCreateRequest,
                                          @AuthenticationPrincipal PrincipalDetail principalDetail) {
        bannerService.saveBanner(bannerImage, bannerCreateRequest, principalDetail);
        return ApiResponse.onSuccess("배너 저장 성공");
    }

    @GetMapping("/{bannerId}")
    @Operation(
            summary = "단일 배너 조회",
            description = "bannerId로 배너를 조회합니다.<br>ADMIN만 접근 가능합니다."
    )
    public ApiResponse<BannerResponse> showBanner(@PathVariable Long bannerId,
                                                  @AuthenticationPrincipal PrincipalDetail principalDetail) {
        BannerResponse bannerResponse = bannerService.showBanner(bannerId, principalDetail);
        return ApiResponse.onSuccess(bannerResponse);
    }
}
