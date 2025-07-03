package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.dto.response.AdminWinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.AdminWineResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

public class AdminWineServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private AdminWineService adminWineService;

    @DisplayName("와인 이름, 종류, 품종, 국가로 와인 검색 테스트")
    @Test
    void searchWinesAdmin() {
        // Given
        List<Wine> wineList = save8Wines();
        Pageable pageable = PageRequest.of(0, 7);

        // when
        PageResponse<AdminWinePreviewResponse> adminWineResponsePageResponse = adminWineService.searchWinesAdmin("와인", "레드", "피노누아", "프랑스", pageable);

        // then
        assertWinePreviewPageResponse(adminWineResponsePageResponse, 0, 1, List.of(
                AdminWinePreviewResponse.of(wineList.get(0)),
                AdminWinePreviewResponse.of(wineList.get(3))
        ));
    }

    @DisplayName("와인 이름으로 와인 검색 테스트")
    @Test
    void searchWinesAdminByName() {
        // Given
        List<Wine> wineList = save8Wines();
        Pageable pageable = PageRequest.of(0, 7);

        // when
        PageResponse<AdminWinePreviewResponse> adminWineResponsePageResponse = adminWineService.searchWinesAdmin("와인", null, null, null, pageable);

        // then
        assertWinePreviewPageResponse(adminWineResponsePageResponse, 0, 2, List.of(
                AdminWinePreviewResponse.of(wineList.get(0)),
                AdminWinePreviewResponse.of(wineList.get(1)),
                AdminWinePreviewResponse.of(wineList.get(2)),
                AdminWinePreviewResponse.of(wineList.get(3)),
                AdminWinePreviewResponse.of(wineList.get(4)),
                AdminWinePreviewResponse.of(wineList.get(5)),
                AdminWinePreviewResponse.of(wineList.get(6))
        ));
    }

    @DisplayName("와인 종류로 와인 검색 테스트")
    @Test
    void searchWinesAdminBySort() {
        // Given
        List<Wine> wineList = save8Wines();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<AdminWinePreviewResponse> adminWineResponsePageResponse = adminWineService.searchWinesAdmin(null, "레드", null, null, pageable);

        // then
        assertWinePreviewPageResponse(adminWineResponsePageResponse, 0, 1, List.of(
                AdminWinePreviewResponse.of(wineList.get(0)),
                AdminWinePreviewResponse.of(wineList.get(3)),
                AdminWinePreviewResponse.of(wineList.get(6)
                )));
    }

    @DisplayName("와인 품종으로 와인 검색 테스트")
    @Test
    void searchWinesAdminByVariety() {
        // Given
        List<Wine> wineList = save8Wines();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<AdminWinePreviewResponse> adminWineResponsePageResponse = adminWineService.searchWinesAdmin(null, null, "피노누아", null, pageable);

        // then
        assertWinePreviewPageResponse(adminWineResponsePageResponse, 0, 1, List.of(
                AdminWinePreviewResponse.of(wineList.get(0)),
                AdminWinePreviewResponse.of(wineList.get(2)),
                AdminWinePreviewResponse.of(wineList.get(3)),
                AdminWinePreviewResponse.of(wineList.get(5))
        ));
    }

    @DisplayName("와인 id로 와인 상세 조회 테스트")
    @Test
    void getWine() {
        // Given
        Wine wine = createWine("와인1", "wine1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        wineRepository.save(wine);

        // when
        AdminWineResponse adminWineResponse = adminWineService.getWine(wine.getId());

        // then
        Assertions.assertThat(adminWineResponse.getAdminWinePreviewResponse())
                .extracting("wineId", "name", "sort", "variety", "country", "region", "createdAt")
                .containsExactly(wine.getId(), wine.getName(), wine.getSort(),wine.getVariety(), wine.getCountry(), wine.getRegion(), wine.getCreatedAt());
        Assertions.assertThat(adminWineResponse.getAdminWineDetailResponse())
                .extracting("wineId", "name", "nameEng","imageUrl", "sort", "country", "region", "variety", "vivinoRating", "price")
                .containsExactly(wine.getId(), wine.getName(), wine.getNameEng(), wine.getImageUrl(), wine.getSort(), wine.getCountry(), wine.getRegion(), wine.getVariety(), wine.getVivinoRating(), wine.getPrice());
    }

    @DisplayName("존재하지 않는 와인 id로 와인 상세 조회 테스트")
    @Test
    void getWineNotExist() {
        // when // then
        assertThatThrownBy(() -> adminWineService.getWine(0L))
                .isInstanceOf(GeneralException.class)
                .hasMessage("와인이 없습니다.");
    }

    private List<Wine> save8Wines() {
        Wine wine1 = createWine("와인1", "wine1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "wine2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "wine3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "wine4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "wine5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "wine6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "wine7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "wine8", "화이트", "독일", 250000, "리슬링", 4.3f);
        List<Wine> wineList = List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8);
        wineRepository.saveAll(wineList);
        return wineList;
    }

    private Wine createWine(String name, String nameEng, String sort, String country, int price, String variety, float vivinoRating) {
        return Wine.builder()
                .name(name)
                .nameEng(nameEng)
                .imageUrl("http://default.image")
                .sort(sort)
                .country(country)
                .variety(variety)
                .vivinoRating(vivinoRating)
                .searchName(name.replaceAll("[ ,.'\\\\]", "").toLowerCase()
                        .concat(nameEng.replaceAll("[ ,.'\\\\]", "").toLowerCase()))
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(price).build();
    }

    private void assertWinePreviewPageResponse(PageResponse<AdminWinePreviewResponse> adminWinePreviewResponsePageResponse, int pageNumber, int totalPages, List<AdminWinePreviewResponse> adminWinePreviewResponseList) {
        Assertions.assertThat(adminWinePreviewResponsePageResponse.getContent())
                .hasSize(adminWinePreviewResponseList.size())
                .extracting("wineId", "name", "sort", "country", "region", "createdAt")
                .containsExactly(
                        adminWinePreviewResponseList.stream()
                                .map(content -> tuple(content.getWineId(), content.getName(),
                                        content.getSort(), content.getCountry(), content.getRegion(), content.getCreatedAt()))
                                .toArray(Tuple[]::new)
                );
        Assertions.assertThat(adminWinePreviewResponsePageResponse)
                .extracting("pageNumber", "totalPages")
                .containsExactly(pageNumber, totalPages);
    }
}
