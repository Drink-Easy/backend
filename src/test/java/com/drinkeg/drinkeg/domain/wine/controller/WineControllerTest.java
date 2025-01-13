package com.drinkeg.drinkeg.domain.wine.controller;

import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.UserDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.*;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WineControllerTest extends WineControllerTestSupport {
    @BeforeEach
    void setUp() {
        PrincipalDetail principalDetail = new PrincipalDetail(UserDTO.builder().username("user").build());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principalDetail, "password", principalDetail.getAuthorities())
        );
    }

    @DisplayName("와인 이름으로 와인을 검색한다.")
    @Test
    void searchWineByWineName() throws Exception {
        // given
        String wineName = "와인";
        when(wineService.searchWinesByName(wineName)).thenReturn(
                List.of(creatWinePreviewResponse(1L, "와인1"),
                        creatWinePreviewResponse(2L, "와인2"),
                        creatWinePreviewResponse(3L, "와인3")));
        // when // then
        mockMvc.perform(get("/wine?searchName=" + wineName))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result[0].name").value("와인1"))
                .andExpect(jsonPath("$.result[1].name").value("와인2"))
                .andExpect(jsonPath("$.result[2].name").value("와인3"));
    }

    @DisplayName("와인 이름으로 와인을 검색하는데 일치하는 와인이 없다면 빈 리스트를 반환한다.")
    @Test
    void searchWineByNotExistingWineName() throws Exception {
        // given
        String wineName = "존재하지 않는 와인 이름";
        when(wineService.searchWinesByName(wineName)).thenReturn(
                List.of());
        // when // then
        mockMvc.perform(get("/wine?searchName=" + wineName))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").isEmpty());
    }

    @DisplayName("검색 파라미터를 넣지 않으면 기본값으로 빈 문자열이 들어간다.")
    @Test
    void searchWineByBlankSearchWineParameter() throws Exception {
        // given
        String wineName = "";
        when(wineService.searchWinesByName(wineName)).thenReturn(
                List.of(creatWinePreviewResponse(1L, "와인1"),
                        creatWinePreviewResponse(2L, "와인2"),
                        creatWinePreviewResponse(3L, "와인3")));
        // when // then
        mockMvc.perform(get("/wine"))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result[0].name").value("와인1"))
                .andExpect(jsonPath("$.result[1].name").value("와인2"))
                .andExpect(jsonPath("$.result[2].name").value("와인3"));
    }

    @DisplayName("와인 아이디로 와인 상세 정보를 조회한다.")
    @Test
    void findWineInfoByWineId() throws Exception {
        // given
        WineWithThreeReviewsResponse wineWithThreeReviewsResponse = createWineWithThreeReviewsResponse();
        when(wineService.getWineInfoWithThreeReviews(any(Long.class), any(String.class)))
                .thenReturn(wineWithThreeReviewsResponse);

        // when // then
        mockMvc.perform(get("/wine/{wineId}", 1L)
                        .principal(() -> "user"))  // principal을 통한 인증 정보 전달
                .andDo(print())
                // 응답 코드(HTTP Status) 검증
                .andExpect(status().isOk())
                // 공통 응답 스펙 (code, message 등) 검증
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                // 실제 result 내 데이터 검증 (WineInfoResponse)
                .andExpect(jsonPath("$.result.wineInfoResponse.wineId").value(1L))
                .andExpect(jsonPath("$.result.wineInfoResponse.name").value("테스트 와인"))
                .andExpect(jsonPath("$.result.wineInfoResponse.imageUrl").value("https://test-image-url.png"))
                .andExpect(jsonPath("$.result.wineInfoResponse.price").value(30000))
                .andExpect(jsonPath("$.result.wineInfoResponse.sort").value("레드"))
                .andExpect(jsonPath("$.result.wineInfoResponse.country").value("프랑스"))
                .andExpect(jsonPath("$.result.wineInfoResponse.variety").value("Merlot"))
                .andExpect(jsonPath("$.result.wineInfoResponse.vivinoRating").value(4.3))
                .andExpect(jsonPath("$.result.wineInfoResponse.avgSweetness").value(5.0))
                .andExpect(jsonPath("$.result.wineInfoResponse.avgAcidity").value(4.2))
                .andExpect(jsonPath("$.result.wineInfoResponse.avgTannin").value(3.8))
                .andExpect(jsonPath("$.result.wineInfoResponse.avgBody").value(4.0))
                .andExpect(jsonPath("$.result.wineInfoResponse.avgAlcohol").value(14.0))
                .andExpect(jsonPath("$.result.wineInfoResponse.nose1").value("블랙베리"))
                .andExpect(jsonPath("$.result.wineInfoResponse.nose2").value("초콜릿"))
                .andExpect(jsonPath("$.result.wineInfoResponse.nose3").value("바닐라"))
                .andExpect(jsonPath("$.result.wineInfoResponse.avgMemberRating").value(4.5))
                // isLiked는 Java 명명 규칙에 의해서 liked로 자동 변환된다.
                .andExpect(jsonPath("$.result.wineInfoResponse.liked").value(true))
                // 최근 리뷰 3개 검증
                .andExpect(jsonPath("$.result.recentReviews[0].review").value("첫 번째 리뷰 내용"))
                .andExpect(jsonPath("$.result.recentReviews[0].name").value("user1"))
                .andExpect(jsonPath("$.result.recentReviews[0].rating").value(5))
                .andExpect(jsonPath("$.result.recentReviews[1].review").value("두 번째 리뷰 내용"))
                .andExpect(jsonPath("$.result.recentReviews[1].name").value("user2"))
                .andExpect(jsonPath("$.result.recentReviews[1].rating").value(4))
                .andExpect(jsonPath("$.result.recentReviews[2].review").value("세 번째 리뷰 내용"))
                .andExpect(jsonPath("$.result.recentReviews[2].name").value("user3"))
                .andExpect(jsonPath("$.result.recentReviews[2].rating").value(5));
    }

    @DisplayName("존재하지 않는 와인 아이디로 와인 상세 정보를 조회하면 예외를 반환한다.")
    @Test
    void findWineInfoWithWrongWineId() throws Exception {
        // given
        GeneralException generalException = new GeneralException(ErrorStatus.WINE_NOT_FOUND);
        when(wineService.getWineInfoWithThreeReviews(any(Long.class), any(String.class)))
                .thenThrow(generalException);

        // when // then
        mockMvc.perform(get("/wine/{wineId}", 1L)
                        .principal(() -> "user"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("WINE4001"))
                .andExpect(jsonPath("$.message").value("와인이 없습니다."));
    }

    @DisplayName("와인 아이디와 정렬 기준으로 와인의 리뷰를 전체조회한다.")
    @Test
    void findWineReviewByWineIdAndSortType() throws Exception {
        // given
        Long wineId = 1L;
        String sortType = "최신순";
        when(wineService.getWineReviewsAndIsLikedByWineId(wineId, SortType.of(sortType)))
                .thenReturn(List.of(
                        createReviewResponse("첫 번째 리뷰 내용", "user1", 5, LocalDateTime.of(2025, 1, 6, 0, 0)),
                        createReviewResponse("두 번째 리뷰 내용", "user2", 4, LocalDateTime.of(2025, 1, 6, 0, 0)),
                        createReviewResponse("세 번째 리뷰 내용", "user3", 5, LocalDateTime.of(2025, 1, 6, 0, 0))
                ));
        // when // then
        mockMvc.perform(get("/wine/review/{wineId}?sortType={sortType}", wineId, sortType))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result[0].review").value("첫 번째 리뷰 내용"))
                .andExpect(jsonPath("$.result[0].name").value("user1"))
                .andExpect(jsonPath("$.result[0].rating").value(5))
                .andExpect(jsonPath("$.result[1].review").value("두 번째 리뷰 내용"))
                .andExpect(jsonPath("$.result[1].name").value("user2"))
                .andExpect(jsonPath("$.result[1].rating").value(4))
                .andExpect(jsonPath("$.result[2].review").value("세 번째 리뷰 내용"))
                .andExpect(jsonPath("$.result[2].name").value("user3"))
                .andExpect(jsonPath("$.result[2].rating").value(5));
    }

    @DisplayName("올바르지 않은 정렬 기준으로 와인의 리뷰를 전체조회하면 예외가 반환된다.")
    @Test
    void findWineReviewByWineIdAndWrongSortType() throws Exception {
        // given
        Long wineId = 1L;
        String sortType = "올바르지 않은 정렬 기준";
        // when // then
        mockMvc.perform(get("/wine/review/{wineId}?sortType={sortType}", wineId, sortType))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ARGUMENT_ERROR"))
                .andExpect(jsonPath("$.message").value("유효하지 않은 정렬 타입입니다."));
    }

    @DisplayName("멤버의 취향 정보를 기반으로 추천 와인 리스트를 조회한다.")
    @Test
    void findRecommendWineList() throws Exception {
        // given
        when(wineService.getRecommendWineList("user"))
                .thenReturn(List.of(
                        createHomeWineResponse("와인1"),
                        createHomeWineResponse("와인2"),
                        createHomeWineResponse("와인3"))
                );
        // when // then
        mockMvc.perform(get("/wine/recommend")
                        .principal(() -> "user"))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result[0].wineName").value("와인1"))
                .andExpect(jsonPath("$.result[1].wineName").value("와인2"))
                .andExpect(jsonPath("$.result[2].wineName").value("와인3"));
    }

    @DisplayName("가장 인기있는 와인 10개를 조회한다.")
    @Test
    void findMostLikedWineList() throws Exception {
        // given
        when(wineService.getMostLikedWineList())
                .thenReturn(List.of(createHomeWineResponse("와인1"),
                        createHomeWineResponse("와인2"),
                        createHomeWineResponse("와인3"),
                        createHomeWineResponse("와인4"),
                        createHomeWineResponse("와인5"),
                        createHomeWineResponse("와인6"),
                        createHomeWineResponse("와인7"),
                        createHomeWineResponse("와인8"),
                        createHomeWineResponse("와인9"),
                        createHomeWineResponse("와인10")));
        // when // then
        mockMvc.perform(get("/wine/most-liked"))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result[0].wineName").value("와인1"))
                .andExpect(jsonPath("$.result[1].wineName").value("와인2"))
                .andExpect(jsonPath("$.result[2].wineName").value("와인3"))
                .andExpect(jsonPath("$.result[3].wineName").value("와인4"))
                .andExpect(jsonPath("$.result[4].wineName").value("와인5"))
                .andExpect(jsonPath("$.result[5].wineName").value("와인6"))
                .andExpect(jsonPath("$.result[6].wineName").value("와인7"))
                .andExpect(jsonPath("$.result[7].wineName").value("와인8"))
                .andExpect(jsonPath("$.result[8].wineName").value("와인9"))
                .andExpect(jsonPath("$.result[9].wineName").value("와인10"));
    }

    private HomeWineResponse createHomeWineResponse(String name) {
        return HomeWineResponse.builder()
                .wineId(1L)
                .imageUrl("https://test-image-url.png")
                .wineName(name)
                .sort("레드")
                .price(30000)
                .vivinoRating(4.3f)
                .build();
    }

    private WineWithThreeReviewsResponse createWineWithThreeReviewsResponse() {
        return WineWithThreeReviewsResponse.builder()
                .wineInfoResponse(wineInfoResponse())
                .recentReviews(List.of(
                        createReviewResponse("첫 번째 리뷰 내용", "user1", 5, LocalDateTime.of(2025, 1, 6, 0, 0)),
                        createReviewResponse("두 번째 리뷰 내용", "user2", 4, LocalDateTime.of(2025, 1, 6, 0, 0)),
                        createReviewResponse("세 번째 리뷰 내용", "user3", 5, LocalDateTime.of(2025, 1, 6, 0, 0))
                ))
                .build();
    }

    private WineInfoResponse wineInfoResponse() {
        return WineInfoResponse.builder()
                .wineId(1L)
                .name("테스트 와인")
                .imageUrl("https://test-image-url.png")
                .price(30000)
                .sort("레드")
                .country("프랑스")
                .variety("Merlot")
                .vivinoRating(4.3f)
                .avgSweetness(5.0f)
                .avgAcidity(4.2f)
                .avgTannin(3.8f)
                .avgBody(4.0f)
                .avgAlcohol(14.0f)
                .nose1("블랙베리")
                .nose2("초콜릿")
                .nose3("바닐라")
                .avgMemberRating(4.5f)
                .liked(true)
                .build();
    }

    private WineReviewResponse createReviewResponse(String review, String name, int rating, LocalDateTime createdAt) {
        return WineReviewResponse.builder()
                .review(review)
                .name(name)
                .rating(rating)
                .createdAt(createdAt)
                .build();
    }

    private WinePreviewResponse creatWinePreviewResponse(Long wineId, String name) {
        return WinePreviewResponse.builder()
                .wineId(wineId)
                .name(name)
                .imageUrl("default")
                .sort("default")
                .country("default")
                .variety("default")
                .vivinoRating(4.1f)
                .price(100).build();
    }
}