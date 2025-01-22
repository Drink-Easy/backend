package com.drinkeg.drinkeg.domain.banner.controller;

import com.drinkeg.drinkeg.MockMember;
import com.drinkeg.drinkeg.domain.banner.dto.response.AllBannerResponse;
import com.drinkeg.drinkeg.domain.banner.dto.response.BannerResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BannerControllerTest extends BannerControllerTestSupport{

    @DisplayName("전체 배너를 조회한다.")
    @Test
    @MockMember
    public void findAllBanner() throws Exception {
        // given
        when(bannerService.showAllBanner())
                .thenReturn(
                        createAllBannerResponse(List.of(
                                createBannerResponse(1L, "https://test.s3.amazonaws.com/test1", "www.test1.com"),
                                createBannerResponse(2L, "https://test.s3.amazonaws.com/test2", "www.test2.com")
                        )));
        // when & then
        mockMvc.perform(get("/banner"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.bannerResponseList.length()").value(2))
                .andExpect(jsonPath("$.result.bannerResponseList[0].bannerId").value(1))
                .andExpect(jsonPath("$.result.bannerResponseList[1].bannerId").value(2));
    }

    @DisplayName("전체 배너를 조회할 때 배너가 존재하지 않는다면 빈 배열 bannerResponseList를 반환한다.")
    @Test
    @MockMember
    public void findAllBannerWithEmptyBanner() throws Exception {
        // given
        when(bannerService.showAllBanner())
                .thenReturn(createAllBannerResponse(List.of()));
        // when $ then
        mockMvc.perform(get("/banner"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.bannerResponseList").isArray());
    }

    private BannerResponse createBannerResponse(Long bannerId, String imageUrl, String postUrl) {
        return BannerResponse.builder()
                .bannerId(bannerId)
                .imageUrl(imageUrl)
                .postUrl(postUrl)
                .build();
    }

    private AllBannerResponse createAllBannerResponse(List<BannerResponse> bannerResponseList) {
        return new AllBannerResponse(bannerResponseList);
    }
}
