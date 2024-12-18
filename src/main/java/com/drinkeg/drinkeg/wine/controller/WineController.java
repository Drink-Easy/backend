package com.drinkeg.drinkeg.wine.controller;


import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.wine.service.WineService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine")
public class WineController {

    private final WineService wineService;

    // 검색
    @GetMapping
    @Operation(summary = "와인 검색", description = "와인 이름으로 와인 검색하여 searchWineResponseDTOS로 반환")
    public ApiResponse<List<SearchWineResponseDTO>> searchWine(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                               @RequestParam String searchName) {

        List<SearchWineResponseDTO> searchWineResponseDTOS = wineService.searchWinesByName(searchName, principalDetail);
        return ApiResponse.onSuccess(searchWineResponseDTOS);
    }

    // 선택한 와인 정보 출력
    @GetMapping("/{wineId}")
    @Operation(summary = "선택 와인 정보 열람", description = "선택한 와인의 정보를 wineResponseDTO에 담아 반환")
    public ApiResponse<WineResponseWithThreeReviewsDTO> showWine(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                                 @PathVariable("wineId") Long wineId) {

        WineResponseWithThreeReviewsDTO wineResponseWithThreeReviewsDTO = wineService.getWineResponseByWineId(wineId, principalDetail);

        return ApiResponse.onSuccess(wineResponseWithThreeReviewsDTO);
    }

    // 와인 리뷰 보기
    @GetMapping("/review/{wineId}")
    @Operation(summary = "선택 와인 리뷰 열람", description = "선택한 와인 리뷰를 List로 반환")
    public ApiResponse<WineReviewResponseDTO> showWineReview(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                           @PathVariable("wineId") Long wineId, @RequestParam Boolean orderByLatest) {

        WineReviewResponseDTO wineReviewResponseDTO = wineService.getWineReviewsByWineId(wineId, principalDetail, orderByLatest);


        return ApiResponse.onSuccess(wineReviewResponseDTO);
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
}
