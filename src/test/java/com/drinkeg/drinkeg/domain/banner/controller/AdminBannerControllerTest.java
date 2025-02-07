package com.drinkeg.drinkeg.domain.banner.controller;

import com.drinkeg.drinkeg.domain.banner.dto.request.BannerRequest;
import com.drinkeg.drinkeg.domain.banner.dto.response.BannerResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminBannerControllerTest extends BannerControllerTestSupport{

    @DisplayName("배너 이미지를 S3 버킷에 업로드하고 배너를 저장한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void saveBanner() throws Exception {
        // given
        MockMultipartFile bannerImage = createBannerImage();
        MockMultipartFile bannerRequest = createBannerRequest();
        when(bannerService.saveBanner(eq(bannerImage), any(BannerRequest.class)))
                .thenReturn(1L);
        // when & then
        mockMvc.perform(multipart("/admin/banner")
                        .file(bannerImage) // 이미지 파일 추가
                        .file(bannerRequest) // JSON 데이터 추가
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value(1));
    }

    @DisplayName("배너 생성 시 이미지를 포함하지 않으면 MISSING_REQUEST_PART 에러가 발생한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void saveBannerWithoutBannerImage() throws Exception {
        // given
        MockMultipartFile bannerRequest = createBannerRequest();
        // when & then
        mockMvc.perform(multipart("/admin/banner")
                        .file(bannerRequest) // JSON 데이터 추가
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MISSING_REQUEST_PART"))
                .andExpect(jsonPath("$.message").value("필수 요청 데이터가 누락되었습니다: bannerImage"));
    }

    @DisplayName("배너 생성 시 BannerRequest JSON 본문을 포함하지 않으면 MISSING_REQUEST_PART 에러가 발생한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void saveBannerWithoutBannerRequest() throws Exception {
        // given
        MockMultipartFile bannerImage = createBannerImage();
        // when & then
        mockMvc.perform(multipart("/admin/banner")
                        .file(bannerImage) // 배너 이미지 추가
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MISSING_REQUEST_PART"))
                .andExpect(jsonPath("$.message").value("필수 요청 데이터가 누락되었습니다: banner"));
    }

    @DisplayName("배너 ID로 배너를 조회한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void findBannerById() throws Exception {
        // given
        Long bannerId = 1L;
        when(bannerService.showBanner(bannerId))
                .thenReturn(createBannerResponse(bannerId, "https://test.s3.amazonaws.com/test", "www.test.com"));
        // when & then
        mockMvc.perform(get("/admin/banner/" + bannerId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.bannerId").value(1))
                .andExpect(jsonPath("$.result.imageUrl").value("https://test.s3.amazonaws.com/test"))
                .andExpect(jsonPath("$.result.postUrl").value("www.test.com"));
    }

    @DisplayName("올바르지 않은 ID로 배너를 조회하면 BANNER_NOT_FOUND 예외가 발생한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void findBannerWithWrongId() throws Exception {
        // given
        Long wrongId = 0L;
        when(bannerService.showBanner(wrongId))
                .thenThrow(new GeneralException(ErrorStatus.BANNER_NOT_FOUND));
        // when & then
        mockMvc.perform(get("/admin/banner/" + wrongId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorStatus.BANNER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorStatus.BANNER_NOT_FOUND.getMessage()));
    }

    @DisplayName("배너 ID로 배너를 수정한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateBanner() throws Exception {
        // given
        Long bannerId = 1L;
        MockMultipartFile bannerImage = createBannerImage();
        MockMultipartFile bannerRequest = createBannerRequest();
        // when & then
        mockMvc.perform(multipart("/admin/banner/" + bannerId)
                        .file(bannerImage) // 이미지 파일 추가
                        .file(bannerRequest) // JSON 데이터 추가
                        .with(csrf())
                        .with(request -> { // PATCH method 호출
                            request.setMethod("PATCH");
                            return request;
                        })
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("배너 수정 성공"));
    }

    @DisplayName("배너를 수정할 때 Banner Request JSON 본문만 포함하고 배너 이미지는 포함하지 않아도 수정은 성공한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateBannerWithoutBannerImage() throws Exception {
        // given
        Long bannerId = 1L;
        MockMultipartFile bannerRequest = createBannerRequest();
        // when & then
        mockMvc.perform(multipart("/admin/banner/" + bannerId)
                        .file(bannerRequest) // JSON 데이터 추가
                        .with(csrf())
                        .with(request -> { // PATCH method 호출
                            request.setMethod("PATCH");
                            return request;
                        })
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("배너 수정 성공"));
    }

    @DisplayName("배너를 수정할 때 배너 이미지만 포함하고 Banner Request JSON 본문은 포함하지 않아도 수정은 성공한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateBannerWithoutBannerRequest() throws Exception {
        // given
        Long bannerId = 1L;
        MockMultipartFile bannerImage = createBannerImage();
        // when & then
        mockMvc.perform(multipart("/admin/banner/" + bannerId)
                        .file(bannerImage) // 이미지 파일 추가
                        .with(csrf())
                        .with(request -> { // PATCH method 호출
                            request.setMethod("PATCH");
                            return request;
                        })
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("배너 수정 성공"));
    }

    @DisplayName("올바르지 않은 ID로 배너 수정을 요청하면 BANNER_NOT_FOUND 예외가 발생한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void updateBannerWithWrongId() throws Exception {
        // given
        Long wrongId = 0L;
        MockMultipartFile bannerImage = createBannerImage();
        MockMultipartFile bannerRequest = createBannerRequest();
        doThrow(new GeneralException(ErrorStatus.BANNER_NOT_FOUND))
                .when(bannerService).updateBanner(eq(wrongId), any(MultipartFile.class), any(BannerRequest.class));
        // when & then
        mockMvc.perform(multipart("/admin/banner/" + wrongId)
                        .file(bannerImage) // 이미지 파일 추가
                        .file(bannerRequest) // JSON 데이터 추가
                        .with(csrf())
                        .with(request -> { // PATCH method 호출
                            request.setMethod("PATCH");
                            return request;
                        })
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorStatus.BANNER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorStatus.BANNER_NOT_FOUND.getMessage()));
    }

    @DisplayName("배너 ID로 배너를 삭제한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteBanner() throws Exception {
        // given
        Long bannerId = 1L;
        // when & then
        mockMvc.perform(delete("/admin/banner/" + bannerId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("배너 삭제 성공"));
    }

    @DisplayName("올바르지 않은 ID로 배너 삭제를 요청하면 BANNER_NOT_FOUND 예외가 발생한다.")
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteBannerWithWrongId() throws Exception {
        // given
        Long wrongId = 0L;
        doThrow(new GeneralException(ErrorStatus.BANNER_NOT_FOUND))
                .when(bannerService).deleteBanner(eq(wrongId));
        // when & then
        mockMvc.perform(delete("/admin/banner/" + wrongId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorStatus.BANNER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorStatus.BANNER_NOT_FOUND.getMessage()));
    }

    // Mock 이미지 생성
    private MockMultipartFile createBannerImage() {
        return new MockMultipartFile(
                "bannerImage", // 컨트롤러의 파라미터 이름
                "test-image.jpg", // 파일 이름
                MediaType.IMAGE_JPEG_VALUE, // MIME 타입
                "Test Image Content".getBytes() // 파일 내용
        );
    }

    // RequestPart로 사용하기 위한 JSON BannerRequest 생성
    private MockMultipartFile createBannerRequest() throws Exception {
        String bannerRequestJson = objectMapper.writeValueAsString(
                BannerRequest.builder()
                        .postUrl("www.test.com")
                        .build()
        );

        return new MockMultipartFile(
                "banner", // 컨트롤러의 파라미터 이름
                "bannerRequest.json", // 파일 이름 (형식상 필요)
                MediaType.APPLICATION_JSON_VALUE, // MIME 타입
                bannerRequestJson.getBytes() // JSON 문자열 데이터
        );
    }

    private BannerResponse createBannerResponse(Long bannerId, String imageUrl, String postUrl) {
        return BannerResponse.builder()
                .bannerId(bannerId)
                .imageUrl(imageUrl)
                .postUrl(postUrl)
                .build();
    }
}
