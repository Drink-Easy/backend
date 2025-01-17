package com.drinkeg.drinkeg.domain.wineWishlist.controller;

import com.drinkeg.drinkeg.MockMember;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WineWishlistControllerTest extends WineWishlistControllerTestSupport {

    @DisplayName("위시리스트 추가 요청이 들어오면 위시리스트에 추가한다.")
    @Test
    @MockMember
    void saveWineWishlistByWineIdAndUser() throws Exception {
        // given
        Long wineId = 1L;
        when(wineWishlistService.createWineWishlist(wineId, "user"))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(post("/wine-wishlist/{wineId}", wineId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("와인 위시리스트 담기 성공"));
    }

    @DisplayName("존재하지 않는 와인에 대해 위시리스트 요청이 들어오면 예외가 발생한다..")
    @Test
    @MockMember
    void saveWineWishlistByWrongWine() throws Exception {
        // given
        when(wineWishlistService.createWineWishlist(-1L, "user"))
                .thenThrow(new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        // when // then
        mockMvc.perform(post("/wine-wishlist/{wineId}", -1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("WINE4001"))
                .andExpect(jsonPath("$.message").value("와인이 없습니다."));
    }

    @DisplayName("이미 존재하는 위시리스트에 대해 추가 요청이 들어오면 예외가 발생한다.")
    @Test
    @MockMember
    void saveDuplicatedWineWishlist() throws Exception {
        // given
        GeneralException generalException = new GeneralException(ErrorStatus.WINE_WISHLIST_ALREADY_EXISTS);
        when(wineWishlistService.createWineWishlist(1L, "user"))
                .thenThrow(generalException);

        // when // then
        mockMvc.perform(post("/wine-wishlist/{wineId}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("WINE_WISHLIST4003"))
                .andExpect(jsonPath("$.message").value("이미 존재하는 위시리스트입니다."));
    }

    @DisplayName("위시리스트 조회 요청이 들어오면 전체 위시리스트를 반환한다.")
    @Test
    @MockMember
    void getWineWishlistByUser() throws Exception {
        // given
        List<WinePreviewResponse> winePreviewResponses = List.of(
                createWinePreviewResponse(1L, "와인1", "wine1", "https://image1", "레드", "프랑스", "보르도", "메를로", 4.5f, 10000),
                createWinePreviewResponse(2L, "와인2", "wine2", "https://image2", "화이트", "이탈리아", "피에몬테", "네비올로", 4.0f, 20000)
        );
        when(wineWishlistService.getAllWineWishlistByMember("user"))
                .thenReturn(winePreviewResponses);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/wine-wishlist")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").isArray())

                .andExpect(jsonPath("$.result[0].wineId").value(1))
                .andExpect(jsonPath("$.result[0].name").value("와인1"))
                .andExpect(jsonPath("$.result[0].nameEng").value("wine1"))
                .andExpect(jsonPath("$.result[0].imageUrl").value("https://image1"))
                .andExpect(jsonPath("$.result[0].sort").value("레드"))
                .andExpect(jsonPath("$.result[0].country").value("프랑스"))
                .andExpect(jsonPath("$.result[0].region").value("보르도"))
                .andExpect(jsonPath("$.result[0].variety").value("메를로"))
                .andExpect(jsonPath("$.result[0].vivinoRating").value(4.5))
                .andExpect(jsonPath("$.result[0].price").value(10000))

                .andExpect(jsonPath("$.result[1].wineId").value(2))
                .andExpect(jsonPath("$.result[1].name").value("와인2"))
                .andExpect(jsonPath("$.result[1].nameEng").value("wine2"))
                .andExpect(jsonPath("$.result[1].imageUrl").value("https://image2"))
                .andExpect(jsonPath("$.result[1].sort").value("화이트"))
                .andExpect(jsonPath("$.result[1].country").value("이탈리아"))
                .andExpect(jsonPath("$.result[1].region").value("피에몬테"))
                .andExpect(jsonPath("$.result[1].variety").value("네비올로"))
                .andExpect(jsonPath("$.result[1].vivinoRating").value(4.0))
                .andExpect(jsonPath("$.result[1].price").value(20000));
    }

    @DisplayName("위시리스트가 없는 경우 조회 요청이 들어오면 빈 리스트를 반환한다.")
    @Test
    @MockMember
    void getWineWishlistByUserWhenEmptyWishlist() throws Exception {
        // given
        List<WinePreviewResponse> winePreviewResponses = new ArrayList<>();
        when(wineWishlistService.getAllWineWishlistByMember("user"))
                .thenReturn(winePreviewResponses);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/wine-wishlist")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value(new ArrayList<>()));
    }

    @DisplayName("위시리스트 삭제 요청이 들어오면 위시리스트에서 삭제한다.")
    @Test
    @MockMember
    void deleteWineWishlistByWineIdAndUser() throws Exception {
        // given
        mockMvc.perform(delete("/wine-wishlist/{wineId}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("와인 위시리스트 삭제 완료"));

    }

    @DisplayName("존재하지 않는 와인에 대해 위시리스트 삭제 요청이 들어오면 예외가 발생한다.")
    @Test
    @MockMember
    void deleteWineWishlistByWrongWine() throws Exception {
        // given
        doThrow(new GeneralException(ErrorStatus.WINE_NOT_FOUND))
                .when(wineWishlistService).deleteWineWishlist(-1L, "user");

        // when // then
        mockMvc.perform(delete("/wine-wishlist/{wineId}", -1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("WINE4001"))
                .andExpect(jsonPath("$.message").value("와인이 없습니다."));
    }

    private WinePreviewResponse createWinePreviewResponse(Long wineId, String name, String nameEng, String imageUrl, String sort, String country, String region, String variety, float vivinoRating, int price) {
        return WinePreviewResponse.builder()
                .wineId(wineId)
                .name(name)
                .nameEng(nameEng)
                .imageUrl(imageUrl)
                .sort(sort)
                .country(country)
                .region(region)
                .variety(variety)
                .vivinoRating(vivinoRating)
                .price(price)
                .build();
    }
}
