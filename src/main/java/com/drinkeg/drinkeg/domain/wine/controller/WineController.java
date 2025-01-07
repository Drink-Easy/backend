package com.drinkeg.drinkeg.domain.wine.controller;


import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.wine.service.WineService;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Wine", description = "와인 유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wine")
public class WineController {
    private final WineService wineService;

    // todo: 페이징 구현하기
    @GetMapping
    @Operation(summary = "와인 검색", description = "와인 이름으로 검색하여 와인의 기본 정보를 조회한다.")
    public ApiResponse<List<WinePreviewResponse>> searchWine(@RequestParam(defaultValue = "") String searchName) {

        List<WinePreviewResponse> winePreviewResponses = wineService.searchWinesByName(searchName);

        return ApiResponse.onSuccess(winePreviewResponses);
    }

    @GetMapping("/{wineId}")
    @Operation(summary = "와인 상세정보 조회", description = "와인의 상세정보를 최근 리뷰 3개와 함께 반환한다. nose1,2,3 값은 존재하지 않으면 null이 들어간다.")
    public ApiResponse<WineWithThreeReviewsResponse> findWineById(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                              @PathVariable("wineId") Long wineId) {
        WineWithThreeReviewsResponse wineWithThreeReviewsResponse =
                wineService.getWineInfoWithThreeReviews(wineId, principalDetail.getUsername());

        return ApiResponse.onSuccess(wineWithThreeReviewsResponse);
    }

    // todo: 페이징 구현하기, IOS 에 sort 타입 설명하기.
    @GetMapping("/review/{wineId}")
    @Operation(summary = "와인 리뷰 전체 조회", description = "선택한 와인의 리뷰들을 List에 담아서 반환한다.")
    public ApiResponse<List<WineReviewResponse>> showWineReview(@PathVariable("wineId") Long wineId, @RequestParam String sortType) {
        List<WineReviewResponse> wineReviewResponseList = wineService.getWineReviewsAndIsLikedByWineId(wineId, SortType.of(sortType));

        return ApiResponse.onSuccess(wineReviewResponseList);
    }

    // 와인 이미지 업로드
    @PostMapping("/upload")
    @Operation(summary = "와인 이미지 업로드", description = "백엔드에세 와인 이미지 업로드 하기 위한 API")
    public ApiResponse<?> uploadWineImage() {
        try {
            wineService.uploadWineImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ApiResponse.onSuccess("업로드 성공");
    }

    // 홈화면 추천 와인 반환
    @GetMapping("/recommend")
    @Operation(summary = "홈화면 페이지", description = "추천 와인 10개를 List로 반환")
    public ApiResponse<List<HomeWineResponse>> home(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                    HttpServletResponse response) {

        // 추천 와인 리스트 가져오기
        List<HomeWineResponse> recommendWineList =
                wineService.getRecommendWineList(principalDetail.getUsername());

        // 응답 헤더에 Cache-Control 추가
        response.setHeader("Cache-Control", "max-age=3600, public");

        return ApiResponse.onSuccess(recommendWineList);
    }

    // 홈화면 인기 와인 반환
    @GetMapping("/most-liked")
    @Operation(summary = "홈화면 페이지", description = "인기 와인 10개를 List로 반환")
    public ApiResponse<List<HomeWineResponse>> mostLikedWine(HttpServletResponse response) {

        // 인기 와인 리스트 가져오기
        List<HomeWineResponse> mostLikedWineList = wineService.getMostLikedWineList();

        // 응답 헤더에 Cache-Control 추가
        response.setHeader("Cache-Control", "max-age=3600, public");
        return ApiResponse.onSuccess(mostLikedWineList);
    }
}
