package com.drinkeg.drinkeg.domain.wine.controller;

import com.drinkeg.drinkeg.MockMember;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminWineControllerTest extends AdminWineControllerSupport{

    @DisplayName("와인 검색 요청이 들어오면 와인을 검색한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void searchWine() throws Exception {
        // given
        String searchName = "와인";
        String wineSort = "레드";
        String wineVariety = "메를로";
        String wineCountry = "프랑스";

        // when // then
        mockMvc.perform(get("/admin/wine")
                        .param("searchName", searchName)
                        .param("wineSort", wineSort)
                        .param("wineVariety", wineVariety)
                        .param("wineCountry", wineCountry)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"));
        verify(adminWineService).searchWinesAdmin(eq(searchName), eq(wineSort), eq(wineVariety), eq(wineCountry), any());
    }

    @DisplayName("올바른 와인 등록 DTO와 이미지로 와인 등록 요청이 들어오면 와인을 등록한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWine() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequest();
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("와인 등록 성공"));
        verify(adminWineService).saveWine(refEq(request), refEq(wineImage));
    }

    @DisplayName("와인 이미지 없이 와인 등록 요청이 들어오면 와인을 등록한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithoutImage() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequest();
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("와인 등록 성공"));
        verify(adminWineService).saveWine(refEq(request), isNull());
    }

    @DisplayName("와인 등록 DTO가 null이면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithNullRequest() throws Exception {
        // given
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MISSING_REQUEST_PART"))
                .andExpect(jsonPath("$.message").value("필수 요청 데이터가 누락되었습니다: wineRegisterRequest"));
    }

    @DisplayName("와인 한글 이름이 없으면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithoutName() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequestDetail("", "wine1", 10000, "레드", "프랑스", "보르도", "메를로", 4.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("와인 이름은 필수입니다."));
    }

    @DisplayName("와인 영문 이름이 없으면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithoutNameEng() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequestDetail("와인1", "", 10000, "레드", "프랑스", "보르도", "메를로", 4.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("와인 영문 이름은 필수입니다."));
    }

    @DisplayName("와인 가격이 0보다 작으면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithNegativePrice() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequestDetail("와인1", "wine1", -10000, "레드", "프랑스", "보르도", "메를로", 4.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("가격은 0 이상이어야 합니다."));
    }

    @DisplayName("와인 종류가 없으면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithoutSort() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequestDetail("와인1", "wine1", 10000, "", "프랑스", "보르도", "메를로", 4.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("종류는 필수입니다."));
    }

    @DisplayName("와인 국가가 없으면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithoutCountry() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequestDetail("와인1", "wine1", 10000, "레드", "", "보르도", "메를로", 4.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("국가는 필수입니다."));
    }

    @DisplayName("와인 생산지가 없으면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithoutRegion() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequestDetail("와인1", "wine1", 10000, "레드", "프랑스", "", "메를로", 4.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("생산지는 필수입니다."));
    }

    @DisplayName("와인 품종이 없으면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithoutVariety() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequestDetail("와인1", "wine1", 10000, "레드", "프랑스", "보르도", "", 4.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("품종은 필수입니다."));
    }

    @DisplayName("와인 비비노 평점이 0보다 작으면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithNegativeVivinoRating() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequestDetail("와인1", "wine1", 10000, "레드", "프랑스", "보르도", "메를로", -4.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("비비노 평점은 0보다 커야 합니다."));
    }

    @DisplayName("와인 비비노 평점이 5보다 크면 와인 등록이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWineWithOverVivinoRating() throws Exception {
        // given
        WineRegisterRequest request = createWineRegisterRequestDetail("와인1", "wine1", 10000, "레드", "프랑스", "보르도", "메를로", 5.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineRegisterRequest = createMockMultipartFile("wineRegisterRequest", request);

        // when // then
        mockMvc.perform(multipart("/admin/wine")
                        .file(wineImage)
                        .file(wineRegisterRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("비비노 평점은 5 이하여야 합니다."));
    }

    @DisplayName("와인 수정 DTO와 이미지로 와인 수정 요청이 들어오면 와인을 수정한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void updateWine() throws Exception {
        // given
        Long wineId = 1L;
        WineUpdateRequest request = createWineUpdateRequest();
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineUpdateRequest = createMockMultipartFile("wineUpdateRequest", request);

        // when // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/admin/wine/{wineId}", wineId)
                        .file(wineImage)
                        .file(wineUpdateRequest)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("와인 수정 성공"));
        verify(adminWineService).updateWine(eq(wineId), refEq(request), refEq(wineImage));
    }

    @DisplayName("와인 수정 DTO만 있으면 와인을 수정한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void updateWineWithoutImage() throws Exception {
        // given
        Long wineId = 1L;
        WineUpdateRequest request = createWineUpdateRequest();
        MockMultipartFile wineUpdateRequest = createMockMultipartFile("wineUpdateRequest", request);

        // when // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/admin/wine/{wineId}", wineId)
                        .file(wineUpdateRequest)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("와인 수정 성공"));
        verify(adminWineService).updateWine(eq(wineId), refEq(request), isNull());
    }

    @DisplayName("와인 이미지만 있으면 와인을 수정한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void updateWineWithoutRequest() throws Exception {
        // given
        Long wineId = 1L;
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");

        // when // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/admin/wine/{wineId}", wineId)
                        .file(wineImage)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("와인 수정 성공"));
        verify(adminWineService).updateWine(eq(wineId), isNull(), refEq(wineImage));
    }

    @DisplayName("와인 수정 DTO와 이미지가 없으면 와인 수정이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void updateWineWithNullRequest() throws Exception {
        // given
        Long wineId = 1L;

        // when // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/admin/wine/{wineId}", wineId)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ARGUMENT_ERROR"))
                .andExpect(jsonPath("$.message").value("wineUpdateRequest 또는 wineImage 중 하나는 필수입니다."));
    }

    @DisplayName("와인 가격이 0보다 작으면 와인 수정이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void updateWineWithNegativePrice() throws Exception {
        // given
        Long wineId = 1L;
        WineUpdateRequest request = createWineUpdateRequestDetail("와인2", "wine2", -20000, "화이트", "이탈리아", "로마", "샤르도네", 3.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineUpdateRequest = createMockMultipartFile("wineUpdateRequest", request);

        // when // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/admin/wine/{wineId}", wineId)
                        .file(wineImage)
                        .file(wineUpdateRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("가격은 0 이상이어야 합니다."));
    }

    @DisplayName("와인 비비노 평점이 0보다 작으면 와인 수정이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void updateWineWithNegativeVivinoRating() throws Exception {
        // given
        Long wineId = 1L;
        WineUpdateRequest request = createWineUpdateRequestDetail("와인2", "wine2", 20000, "화이트", "이탈리아", "로마", "샤르도네", -3.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineUpdateRequest = createMockMultipartFile("wineUpdateRequest", request);

        // when // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/admin/wine/{wineId}", wineId)
                        .file(wineImage)
                        .file(wineUpdateRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("비비노 평점은 0보다 커야 합니다."));
    }

    @DisplayName("와인 비비노 평점이 5보다 크면 와인 수정이 실패한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void updateWineWithOverVivinoRating() throws Exception {
        // given
        Long wineId = 1L;
        WineUpdateRequest request = createWineUpdateRequestDetail("와인2", "wine2", 20000, "화이트", "이탈리아", "로마", "샤르도네", 5.5f);
        MockMultipartFile wineImage = createMockMultipartFileImage("test-image.jpg");
        MockMultipartFile wineUpdateRequest = createMockMultipartFile("wineUpdateRequest", request);

        // when // then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/admin/wine/{wineId}", wineId)
                        .file(wineImage)
                        .file(wineUpdateRequest)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("비비노 평점은 5 이하여야 합니다."));
    }

    private WineRegisterRequest createWineRegisterRequest() {
        return WineRegisterRequest.builder()
                .name("와인1")
                .nameEng("wine1")
                .price(10000)
                .sort("레드")
                .country("프랑스")
                .region("보르도")
                .variety("메를로")
                .vivinoRating(4.5f)
                .build();
    }

    private WineRegisterRequest createWineRegisterRequestDetail(String name, String nameEng, int price, String sort, String country, String region, String variety, float vivinoRating) {
        return WineRegisterRequest.builder()
                .name(name)
                .nameEng(nameEng)
                .price(price)
                .sort(sort)
                .country(country)
                .region(region)
                .variety(variety)
                .vivinoRating(vivinoRating)
                .build();
    }

    private WineUpdateRequest createWineUpdateRequest() {
        return WineUpdateRequest.builder()
                .name("와인2")
                .nameEng("wine2")
                .price(20000)
                .sort("화이트")
                .country("이탈리아")
                .region("로마")
                .variety("샤르도네")
                .vivinoRating(3.5f)
                .build();
    }

    private WineUpdateRequest createWineUpdateRequestDetail(String name, String nameEng, Integer price, String sort, String country, String region, String variety, Float vivinoRating) {
        return WineUpdateRequest.builder()
                .name(name)
                .nameEng(nameEng)
                .price(price)
                .sort(sort)
                .country(country)
                .region(region)
                .variety(variety)
                .vivinoRating(vivinoRating)
                .build();
    }

    private MockMultipartFile createMockMultipartFileImage(String fileName) {
        return new MockMultipartFile(
                "wineImage",
                fileName,
                "image/jpeg",
                "dummy content".getBytes()
        );
    }

    private <T> MockMultipartFile createMockMultipartFile(String partName, T request) throws JsonProcessingException {
        return new MockMultipartFile(
                partName,
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)
        );
    }
}