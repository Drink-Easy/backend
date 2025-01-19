package com.drinkeg.drinkeg.domain.banner.controller;

import com.drinkeg.drinkeg.domain.banner.dto.request.BannerRequest;
import com.drinkeg.drinkeg.domain.banner.dto.response.BannerResponse;
import com.drinkeg.drinkeg.domain.banner.service.BannerService;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
                                          @RequestPart(value = "banner") @Valid BannerRequest bannerRequest,
                                          @AuthenticationPrincipal PrincipalDetail principalDetail) {
        bannerService.saveBanner(bannerImage, bannerRequest, principalDetail);
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

    @PatchMapping("/{bannerId}")
    @Operation(
            summary = "배너 업데이트",
            description = "bannerId에 해당하는 배너를 업데이트합니다. 필요한 정보만 갱신 가능합니다." +
                    "<br>ADMIN만 접근 가능합니다."
    )
    public ApiResponse<String> updateBanner(@PathVariable Long bannerId,
                                            @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImage,
                                            @RequestPart(value = "banner", required = false) @Valid BannerRequest bannerRequest,
                                            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        bannerService.updateBanner(bannerId, bannerImage, bannerRequest, principalDetail);
        return ApiResponse.onSuccess("배너 업데이트 성공");
    }

    @DeleteMapping("/{bannerId}")
    @Operation(
            summary = "배너 삭제",
            description = "bannerId로 배너를 삭제합니다.<br>ADMIN만 접근 가능합니다."
    )
    public ApiResponse<String> deleteBanner(@PathVariable Long bannerId,
                                            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        bannerService.deleteBanner(bannerId, principalDetail);
        return ApiResponse.onSuccess("배너 삭제 성공");
    }
}
