package com.drinkeg.drinkeg.domain.wine.controller;

import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.UserDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineInfoResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
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
    @WithMockUser(username = "user")
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
    @WithMockUser(username = "user")
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
    @WithMockUser(username = "user")
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
    @WithMockUser(username = "user")
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
                .andExpect(jsonPath("$.result.wineInfoResponse.area").value("프랑스"))
                .andExpect(jsonPath("$.result.wineInfoResponse.variety").value("Merlot"))
                .andExpect(jsonPath("$.result.wineInfoResponse.vivinoRating").value(4.3))
                .andExpect(jsonPath("$.result.wineInfoResponse.avgSugarContent").value(5.0))
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
                .area("프랑스")
                .variety("Merlot")
                .vivinoRating(4.3f)
                .avgSugarContent(5.0f)
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
                .area("default")
                .variety("default")
                .vivinoRating(4.1f)
                .price(100).build();
    }
}