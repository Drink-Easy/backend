package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineUpdateRequest;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestPropertySource(properties = {
        "default.wine.url=https://mock-bucket.s3.amazonaws.com/default-image.jpg"
})
public class AdminWineServiceImplImplTest extends IntegrationTestSupport {
    @InjectMocks
    private AdminWineServiceImpl adminWineService;
    @Mock
    private WineRepository wineRepository;
    @Mock
    private StorageService storageService;
    @Value("${default.wine.url}")
    private String defaultImageUrl;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adminWineService, "defaultImageUrl", defaultImageUrl);
    }

    @DisplayName("와인 이미지가 있는 경우 와인 저장 테스트")
    @Test
    void saveWineWithImage() {
        // Given
        WineRegisterRequest request = createWineRegisterRequest();
        MultipartFile mockFile = createMockMultipartFile("test-image.jpg");

        when(storageService.uploadFile(any(MultipartFile.class), eq(StoragePathName.WINE)))
                .thenReturn("https://mock-bucket.s3.amazonaws.com/test-image.jpg");
        when(wineRepository.save(any(Wine.class)))
                .thenAnswer(invocation -> invocation.<Wine>getArgument(0));

        // When
        adminWineService.saveWine(request, mockFile);

        // Then
        verify(storageService).uploadFile(mockFile, StoragePathName.WINE);
        verify(wineRepository).save(argThat(wine ->
                matchesSaveWine(wine, "https://mock-bucket.s3.amazonaws.com/test-image.jpg", request)));
    }

    @Test
    @DisplayName("와인 이미지가 없는 경우 기본 이미지로 와인 저장 테스트")
    void saveWineWithoutImage() {
        // Given
        WineRegisterRequest request = createWineRegisterRequest();

        when(wineRepository.save(any(Wine.class)))
                .thenAnswer(invocation -> invocation.<Wine>getArgument(0));

        // When
        adminWineService.saveWine(request, null);

        // Then
        verify(wineRepository).save(argThat(wine -> matchesSaveWine(wine, defaultImageUrl, request)));
        verify(storageService, times(0)).uploadFile(any(MultipartFile.class), eq(StoragePathName.WINE));
    }

    @DisplayName("디폴트 이미지가 아닌 와인에 대해 wineUpdateRequest와 이미지가 모두 주어진 경우 와인 수정 테스트")
    @Test
    void updateWine() {
        // Given
        Wine wine = createWine("https://mock-bucket.s3.amazonaws.com/test-image.jpg");
        WineUpdateRequest request = createWineUpdateRequest();
        MultipartFile mockFile = createMockMultipartFile("test-image2.jpg");

        when(wineRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(wine));
        when(storageService.uploadFile(any(MultipartFile.class), eq(StoragePathName.WINE)))
                .thenReturn("https://mock-bucket.s3.amazonaws.com/test-image2.jpg");

        // When
        adminWineService.updateWine(1L, request, mockFile);

        // Then
        verify(wineRepository).findById(anyLong());
        verify(storageService).uploadFile(mockFile, StoragePathName.WINE);
        verify(storageService).deleteFile(anyString());
    }

    @DisplayName("디폴트 이미지가 아닌 와인에 대해 wineUpdateRequest만 주어진 경우 와인 수정 테스트")
    @Test
    void updateWineWhenImageIsNull() {
        // Given
        Wine wine = createWine("https://mock-bucket.s3.amazonaws.com/test-image.jpg");
        WineUpdateRequest request = createWineUpdateRequest();

        when(wineRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(wine));

        // When
        adminWineService.updateWine(1L, request, null);

        // Then
        verify(wineRepository).findById(anyLong());
        verify(storageService, times(0)).uploadFile(any(MultipartFile.class), eq(StoragePathName.WINE));
        verify(storageService, times(0)).deleteFile(anyString());
    }

    @DisplayName("디폴트 이미지인 와인에 대해 wineUpdateRequest와 이미지가 모두 주어진 경우 와인 수정 테스트")
    @Test
    void updateWineWhenDefaultImageUrl() {
        // Given
        Wine wine = createWine(defaultImageUrl);
        WineUpdateRequest request = createWineUpdateRequest();
        MultipartFile mockFile = createMockMultipartFile("test-image2.jpg");

        when(wineRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(wine));
        when(storageService.uploadFile(any(MultipartFile.class), eq(StoragePathName.WINE)))
                .thenReturn("https://mock-bucket.s3.amazonaws.com/test-image2.jpg");

        // When
        adminWineService.updateWine(1L, request, mockFile);

        // Then
        verify(wineRepository).findById(anyLong());
        verify(storageService).uploadFile(mockFile, StoragePathName.WINE);
        verify(storageService, times(0)).deleteFile(anyString());
    }

    @DisplayName("디폴트 이미지인 와인에 대해 wineUpdateRequest만 주어진 경우 와인 수정 테스트")
    @Test
    void updateWineWhenDefaultImageUrlAndImageIsNull() {
        // Given
        Wine wine = createWine(defaultImageUrl);
        WineUpdateRequest request = createWineUpdateRequest();

        when(wineRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(wine));

        // When
        adminWineService.updateWine(1L, request, null);

        // Then
        verify(wineRepository).findById(anyLong());
        verify(storageService, times(0)).uploadFile(any(MultipartFile.class), eq(StoragePathName.WINE));
        verify(storageService, times(0)).deleteFile(anyString());
    }

    @DisplayName("수정할 와인 이미지 업로드 중 에러가 발생한 경우 와인 저장 테스트")
    @Test
    void saveWineWhenUploadImageFailed() {
        // Given
        WineRegisterRequest request = createWineRegisterRequest();
        MultipartFile mockFile = createMockMultipartFile("test-image2.jpg");

        when(storageService.uploadFile(any(MultipartFile.class), eq(StoragePathName.WINE)))
                .thenThrow(new GeneralException(ErrorStatus.FILE_UPLOAD_FAILED));

        // When // Then
        assertThrows(GeneralException.class, () -> adminWineService.saveWine(request, mockFile));
    }

    @DisplayName("기존 와인 이미지 삭제 중 에러가 발생한 경우 와인 수정 테스트")
    @Test
    void updateWineWhenDeleteImageFailed() {
        // Given
        Wine wine = createWine("https://mock-bucket.s3.amazonaws.com/test-image.jpg");
        WineUpdateRequest request = createWineUpdateRequest();
        MultipartFile mockFile = createMockMultipartFile("test-image2.jpg");

        when(wineRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.of(wine));
        when(storageService.uploadFile(any(MultipartFile.class), eq(StoragePathName.WINE)))
                .thenReturn("https://mock-bucket.s3.amazonaws.com/test-image2.jpg");
        doThrow(new GeneralException(ErrorStatus.FILE_DELETE_FAILED))
                .when(storageService).deleteFile(anyString());

        // When // Then
        assertThrows(GeneralException.class, () -> adminWineService.updateWine(1L, request, mockFile));
    }

    @DisplayName("수정할 와인이 존재하지 않는 경우 와인 수정 테스트")
    @Test
    void updateWineWhenWineNotFound() {
        // Given
        WineUpdateRequest request = createWineUpdateRequest();
        MultipartFile mockFile = createMockMultipartFile("test-image2.jpg");

        when(wineRepository.findById(anyLong()))
                .thenReturn(java.util.Optional.empty());

        // When // Then
        assertThrows(GeneralException.class, () -> adminWineService.updateWine(1L, request, mockFile));
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

    private boolean matchesSaveWine(Wine wine, String imageUrl, WineRegisterRequest request) {
        String cleanedName = wine.getName().replaceAll("[ ,.'\\\\]", "").toLowerCase();
        String cleanedNameEng = wine.getNameEng().replaceAll("[ ,.'\\\\]", "").toLowerCase();
        String searchName = cleanedName.concat(cleanedNameEng);
        return wine.getName().equals(request.getName()) &&
                wine.getImageUrl().equals(imageUrl) &&
                wine.getNameEng().equals(request.getNameEng()) &&
                wine.getPrice() == request.getPrice() &&
                wine.getSort().equals(request.getSort()) &&
                wine.getCountry().equals(request.getCountry()) &&
                wine.getRegion().equals(request.getRegion()) &&
                wine.getVariety().equals(request.getVariety()) &&
                wine.getVivinoRating() == request.getVivinoRating() &&
                wine.getWineNoteStatistics().getNose1() == null &&
                wine.getWineNoteStatistics().getNose2() == null &&
                wine.getWineNoteStatistics().getNose3() == null &&
                wine.getWineNoteStatistics().getAvgAcidity() == 0 &&
                wine.getWineNoteStatistics().getAvgBody() == 0 &&
                wine.getWineNoteStatistics().getAvgTannin() == 0 &&
                wine.getWineNoteStatistics().getAvgSweetness() == 0 &&
                wine.getWineNoteStatistics().getAvgAlcohol() == 0 &&
                wine.getWineNoteStatistics().getAvgMemberRating() == 0 &&
                wine.getSearchName().equals(searchName);
    }

}
