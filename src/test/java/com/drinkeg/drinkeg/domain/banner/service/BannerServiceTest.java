package com.drinkeg.drinkeg.domain.banner.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.banner.domain.Banner;
import com.drinkeg.drinkeg.domain.banner.dto.request.BannerRequest;
import com.drinkeg.drinkeg.domain.banner.dto.response.AllBannerResponse;
import com.drinkeg.drinkeg.domain.banner.dto.response.BannerResponse;
import com.drinkeg.drinkeg.domain.banner.repository.BannerRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class BannerServiceTest extends IntegrationTestSupport {
    @Autowired
    private BannerService bannerService;

    @Autowired
    private BannerRepository bannerRepository;

    @DisplayName("배너 등록 요청이 오면 배너를 저장한다.")
    @Test
    void saveBanner() {
        // given
        byte[] imageBytes = "test-image-content".getBytes();
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",             // 파일 필드 이름
                "test-image.png",    // 원본 파일 이름
                "image/png",         // MIME 타입
                imageBytes           // 파일 내용
        );
        BannerRequest bannerRequest = createBannerRequest("www.test.com");
        // when
        Long savedId = bannerService.saveBanner(imageFile, bannerRequest);
        // then
        Optional<Banner> banner = bannerRepository.findById(savedId);
        assertThat(banner).isPresent();
    }

    @DisplayName("배너 ID로 조회하면 일치하는 배너를 반환한다.")
    @Test
    public void findBannerById() {
        // given
        Banner save = createBanner("https://test.s3.amazonaws.com/test", "www.test.com");
        bannerRepository.save(save);
        // when
        BannerResponse banner = bannerService.showBanner(save.getId());
        // then
        assertThat(banner).extracting("bannerId", "imageUrl", "postUrl")
                .containsExactly(
                        banner.getBannerId(),
                        "https://test.s3.amazonaws.com/test",
                        "www.test.com"
                );
    }

    @DisplayName("올바르지 않은 ID로 조회하면 BANNER_NOT_FOUND 예외가 발생한다.")
    @Test
    public void findBannerWithWrongId() {
        // given $ when $ then
        assertThatThrownBy(() -> bannerService.showBanner(0L))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.BANNER_NOT_FOUND.getMessage());
    }

    @DisplayName("배너를 조회하면 전체 배너를 반환한다.")
    @Test
    public void findAllBanner() {
        // given
        Banner banner1 = createBanner("https://test.s3.amazonaws.com/test1", "www.test1.com");
        Banner banner2 = createBanner("https://test.s3.amazonaws.com/test2", "www.test2.com");
        Banner banner3 = createBanner("https://test.s3.amazonaws.com/test3", "www.test3.com");
        Banner banner4 = createBanner("https://test.s3.amazonaws.com/test4", "www.test4.com");
        Banner banner5 = createBanner("https://test.s3.amazonaws.com/test5", "www.test5.com");
        bannerRepository.saveAll(List.of(banner1, banner2, banner3, banner4, banner5));
        // when
        AllBannerResponse allBanner = bannerService.showAllBanner();
        // then
        List<BannerResponse> banners = allBanner.getBannerResponseList();
        assertThat(banners).hasSize(5)
                .extracting("bannerId", "imageUrl", "postUrl")
                .containsExactly(
                        tuple(banner5.getId(), "https://test.s3.amazonaws.com/test5", "www.test5.com"),
                        tuple(banner4.getId(), "https://test.s3.amazonaws.com/test4", "www.test4.com"),
                        tuple(banner3.getId(), "https://test.s3.amazonaws.com/test3", "www.test3.com"),
                        tuple(banner2.getId(), "https://test.s3.amazonaws.com/test2", "www.test2.com"),
                        tuple(banner1.getId(), "https://test.s3.amazonaws.com/test1", "www.test1.com")
                );
    }

    @DisplayName("배너 수정 요청이 오면 배너를 수정한다.")
    @Test
    public void updateBanner() {
        // given
        Banner banner = createBanner("https://test.s3.amazonaws.com/test", "www.test.com");
        bannerRepository.save(banner);

        byte[] imageBytes = "test-image-content".getBytes();
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",             // 파일 필드 이름
                "test-image.png",    // 원본 파일 이름
                "image/png",         // MIME 타입
                imageBytes           // 파일 내용
        );
        BannerRequest bannerRequest = createBannerRequest("www.test-update.com");
        // when
        bannerService.updateBanner(banner.getId(), imageFile, bannerRequest);
        // then
        Banner updateBanner = bannerRepository.findById(banner.getId()).get();
        // 기존 imageUrl과 다른지 검증
        assertThat(updateBanner.getImageUrl()).isNotEqualTo("https://test.s3.amazonaws.com/test");
        // postUrl이 "www.test-update.com"과 동일한지 검증
        assertThat(updateBanner.getPostUrl()).isEqualTo("www.test-update.com");
    }

    @DisplayName("배너 수정 요청에 배너 이미지만 포함할 경우 배너 이미지만 수정한다.")
    @Test
    public void updateBannerImage() {
        // given
        Banner banner = createBanner("https://test.s3.amazonaws.com/test", "www.test.com");
        bannerRepository.save(banner);

        byte[] imageBytes = "test-image-content".getBytes();
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",             // 파일 필드 이름
                "test-image.png",    // 원본 파일 이름
                "image/png",         // MIME 타입
                imageBytes           // 파일 내용
        );
        // when
        bannerService.updateBanner(banner.getId(), imageFile, null);
        // then
        Banner updateBanner = bannerRepository.findById(banner.getId()).get();
        assertThat(updateBanner.getImageUrl()).isNotEqualTo("https://test.s3.amazonaws.com/test");
        assertThat(updateBanner.getPostUrl()).isEqualTo("www.test.com");
    }

    @DisplayName("배너 수정 요청에 사진을 제외한 배너 세부사항만 포함할 경우 배너 세부사항만 수정한다.")
    @Test
    public void updateBannerDetail() {
        // given
        Banner banner = createBanner("https://test.s3.amazonaws.com/test", "www.test.com");
        bannerRepository.save(banner);

        BannerRequest bannerRequest = createBannerRequest("www.test-update.com");
        // when
        bannerService.updateBanner(banner.getId(), null, bannerRequest);
        // then
        Banner updateBanner = bannerRepository.findById(banner.getId()).get();
        assertThat(updateBanner).extracting("imageUrl", "postUrl")
                .containsExactly(
                        "https://test.s3.amazonaws.com/test",
                        "www.test-update.com"
                );
    }

    @DisplayName("올바르지 않은 ID로 배너 수정 요청이 오면 BANNER_NOT_FOUND 예외가 발생한다.")
    @Test
    public void updateBannerWithWrongId() {
        // given
        Banner banner = createBanner("https://test.s3.amazonaws.com/test", "www.test.com");
        bannerRepository.save(banner);

        byte[] imageBytes = "test-image-content".getBytes();
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",             // 파일 필드 이름
                "test-image.png",    // 원본 파일 이름
                "image/png",         // MIME 타입
                imageBytes           // 파일 내용
        );
        BannerRequest bannerRequest = createBannerRequest("www.test-update.com");
        // when & then
        assertThatThrownBy(() -> bannerService.updateBanner(0L, imageFile, bannerRequest))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.BANNER_NOT_FOUND.getMessage());
    }

    @DisplayName("배너 삭제 요청이 오면 배너를 삭제한다.")
    @Test
    public void deleteBanner() {
        // given
        Banner banner = createBanner("https://test.s3.amazonaws.com/test", "www.test.com");
        bannerRepository.save(banner);
        // when
        bannerService.deleteBanner(banner.getId());
        // then
        assertThat(bannerRepository.existsById(banner.getId())).isFalse();
    }

    @DisplayName("올바르지 않은 ID로 배너 삭제 요청이 오면 BANNER_NOT_FOUND 예외가 발생한다.")
    @Test
    public void deleteBannerWithWrongId() {
        // given
        Banner banner = createBanner("https://test.s3.amazonaws.com/test", "www.test.com");
        bannerRepository.save(banner);
        // when % then
        assertThatThrownBy(() -> bannerService.deleteBanner(0L))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.BANNER_NOT_FOUND.getMessage());
    }

    private BannerRequest createBannerRequest(String postUrl) {
        return BannerRequest.builder()
                .postUrl(postUrl)
                .build();
    }

    private Banner createBanner(String imageUrl, String postUrl) {
        return Banner.builder()
                .imageUrl(imageUrl)
                .postUrl(postUrl)
                .build();
    }
}
