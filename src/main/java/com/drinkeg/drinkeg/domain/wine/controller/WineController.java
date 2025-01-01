package com.drinkeg.drinkeg.domain.wine.controller;


import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponseDTO;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.domain.wine.service.WineService;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Wine", description = "와인 관련 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wine")
public class WineController {

    private final WineService wineService;

    // 검색
    @GetMapping
    @Operation(summary = "와인 검색", description = "와인 이름으로 와인 검색하여 List<WinePreviewResponseDTO> 반환")
    public ApiResponse<List<WinePreviewResponseDTO>> searchWine(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                                @RequestParam String searchName) {

        List<WinePreviewResponseDTO> winePreviewResponseDTOS = wineService.searchWinesByName(searchName, principalDetail);
        return ApiResponse.onSuccess(winePreviewResponseDTOS);
    }

    // 선택한 와인 정보 출력
    @GetMapping("/{wineId}")
    @Operation(summary = "선택 와인 정보 열람", description = "선택한 와인의 정보를 wineResponseDTO에 담아 반환")
    public ApiResponse<WineResponseWithThreeReviewsDTO> showWine(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                                 @PathVariable("wineId") Long wineId) {

        WineResponseWithThreeReviewsDTO wineResponseWithThreeReviewsDTO = wineService.getWineResponseByWineId(wineId, principalDetail);

        return ApiResponse.onSuccess(wineResponseWithThreeReviewsDTO);
    }

    // 전체 와인 리뷰 보기
    @GetMapping("/review/{wineId}")
    @Operation(summary = "선택 와인 리뷰 열람", description = "선택한 와인 리뷰를 List로 반환")
    public ApiResponse<List<WineReviewResponseDTO>> showWineReview(@PathVariable("wineId") Long wineId, @RequestParam Boolean orderByLatest) {

        List<WineReviewResponseDTO> wineReviewResponseDTOList = wineService.getWineReviewsAndIsLikedByWineId(wineId, orderByLatest);

        return ApiResponse.onSuccess(wineReviewResponseDTOList);
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
    public ApiResponse<List<HomeWineDTO>> home(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                               HttpServletResponse response) {

        // 추천 와인 리스트 가져오기
        List<HomeWineDTO> recommendWineList = wineService.getRecommendWineList(principalDetail);

        // 응답 헤더에 Cache-Control 추가
        response.setHeader("Cache-Control", "max-age=3600, public");

        return ApiResponse.onSuccess(recommendWineList);
    }

    // 홈화면 인기 와인 반환
    @GetMapping("/most-liked")
    @Operation(summary = "홈화면 페이지", description = "인기 와인 10개를 List로 반환")
    public ApiResponse<List<HomeWineDTO>> mostLikedWine(HttpServletResponse response) {

        // 인기 와인 리스트 가져오기
        List<HomeWineDTO> mostLikedWineList = wineService.getMostLikedWineList();

        // 응답 헤더에 Cache-Control 추가
        response.setHeader("Cache-Control", "max-age=3600, public");
        return ApiResponse.onSuccess(mostLikedWineList);
    }
}
