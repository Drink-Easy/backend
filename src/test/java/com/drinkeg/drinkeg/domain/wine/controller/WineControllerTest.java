package com.drinkeg.drinkeg.domain.wine.controller;

import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class WineControllerTest extends WineControllerTestSupport {
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