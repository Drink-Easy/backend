package com.drinkeg.drinkeg.domain.wine.controller;

import com.drinkeg.drinkeg.MockMember;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineUpdateRequest;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class AdminWineControllerTest extends WineControllerTestSupport{

    @DisplayName("와인 등록 요청이 들어오면 와인을 등록한다.")
    @Test
    @MockMember(role = Role.ROLE_ADMIN)
    void saveWine() {
        // given
        WineRegisterRequest request = createWineRegisterRequest();
        MultipartFile mockFile = createMockMultipartFile("test-image.jpg");
        // when
        // then
    }

    private Wine createWine(String imageUrl) {
        return Wine.builder()
                .imageUrl(imageUrl)
                .name("와인1")
                .nameEng("wine1")
                .price(10000)
                .sort("레드")
                .country("프랑스")
                .region("보르도")
                .variety("메를로")
                .vivinoRating(4.5f)
                .wineNoteStatistics(WineNoteStatistics.create())
                .build();
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

    private MultipartFile createMockMultipartFile(String fileName) {
        return new MockMultipartFile(
                "file",
                fileName,
                "image/jpeg",
                "dummy content".getBytes()
        );
    }
}