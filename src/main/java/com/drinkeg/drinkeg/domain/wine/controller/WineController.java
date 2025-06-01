package com.drinkeg.drinkeg.domain.wine.controller;


import com.drinkeg.drinkeg.domain.wine.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.wine.service.WineService;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wine", description = "와인 유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wine")
@Validated
public class WineController {
    private final WineService wineService;

    @GetMapping
    @Operation(summary = "와인 검색", description = "와인 이름 또는 영어 이름으로 검색하여 와인의 기본 정보를 조회한다. " +
            "paging 기능의 sort는 디폴트 값(name)을 사용하는 것을 권장한다.")
    public ApiResponse<PageResponse<WinePreviewResponse>> searchWine(
            @RequestParam(defaultValue = "") String searchName,
            @ParameterObject @PageableDefault(size = 10, sort = "name") Pageable pageable) {

        PageResponse<WinePreviewResponse> pageResponse = wineService.searchWinesByName(searchName, pageable);

        return ApiResponse.onSuccess(pageResponse);
    }

    @GetMapping("/{wineId}")
    @Operation(summary = "와인 상세정보 조회", description = "와인의 상세정보를 최근 리뷰 3개와 함께 반환한다. nose1,2,3 값은 존재하지 않으면 null이 들어간다.")
    public ApiResponse<WineWithThreeReviewsResponse> findWineById(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("wineId") Long wineId,
            @RequestParam(required = false)
            @Range(min = 1970, max = 2024, message = "빈티지는 1970~2024 사이여야 합니다.")
            Integer vintageYear) {

        WineWithThreeReviewsResponse wineWithThreeReviewsResponse =
                wineService.getWineInfoWithThreeReviews(wineId, vintageYear, principalDetail.getUsername());

        return ApiResponse.onSuccess(wineWithThreeReviewsResponse);
    }

    @GetMapping("/review/{wineId}")
    @Operation(summary = "와인 리뷰 전체 조회", description = "선택한 와인의 리뷰들을 List에 담아서 반환한다. " +
            " 정렬 기준(sortType)은 \"최신순\", \"오래된 순\",\" 별점 높은 순\", \"별점 낮은 순\"으로 설정할 수 있다. "
    +"paging 기능의 sort는 사용하지 않고 sortType을 사용한다.")
    public ApiResponse<PageResponse<WineReviewResponse>> showWineReview(
            @PathVariable("wineId") Long wineId,
            @Range(min = 1970, max = 2024, message = "빈티지는 1970~2024 사이여야 합니다.")
            Integer vintageYear,
            @RequestParam String sortType,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {

        PageResponse<WineReviewResponse> wineReviewsPageResponse = wineService
                .getWineReviewsByWineIdAndVintageYear(
                        wineId, vintageYear, SortType.of(sortType), pageable);

        return ApiResponse.onSuccess(wineReviewsPageResponse);
    }

    @GetMapping("/recommend")
    @Operation(summary = "홈화면 페이지 추천 와인 조회", description = "추천 와인 10개를 List에 담아서 반환한다.")
    public ApiResponse<List<HomeWineResponse>> home(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                    HttpServletResponse response) {
        List<HomeWineResponse> recommendWineList = wineService.getRecommendWineList(principalDetail.getUsername());

        response.setHeader("Cache-Control", "max-age=3600, public");

        return ApiResponse.onSuccess(recommendWineList);
    }

    @GetMapping("/most-liked")
    @Operation(summary = "홈화면 페이지 인기 와인 조회", description = "인기 와인 10개를 List에 담아서 반환한다.")
    public ApiResponse<List<HomeWineResponse>> mostLikedWine(HttpServletResponse response) {

        List<HomeWineResponse> mostLikedWineList = wineService.getMostLikedWineList();

        response.setHeader("Cache-Control", "max-age=3600, public");
        return ApiResponse.onSuccess(mostLikedWineList);
    }
}
