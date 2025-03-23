package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.dto.response.AdminWinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.tuple;

public class AdminWineSearchServiceImplTest  extends IntegrationTestSupport {

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private AdminWineService adminWineService;

    @DisplayName("와인 이름, 종류, 품종, 국가로 와인 검색 테스트")
    @Test
    void searchWinesAdmin() {
        // Given
        Wine wine1 = createWine("와인1", "wine1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "wine2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "wine3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "wine4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "wine5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "wine6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "wine7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "wine8", "화이트", "독일", 250000, "리슬링", 4.3f);
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<AdminWinePreviewResponse> adminWineResponsePageResponse = adminWineService.searchWinesAdmin("와인", "레드", "피노누아", "프랑스", pageable);

        // then
        assertWinePreviewPageResponse(adminWineResponsePageResponse, 0, 1, List.of(
                AdminWinePreviewResponse.of(wine1),
                AdminWinePreviewResponse.of(wine4)
        ));
    }

    @DisplayName("와인 이름으로 와인 검색 테스트")
    @Test
    void searchWinesAdminByName() {
        // Given
        Wine wine1 = createWine("와인1", "wine1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "wine2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "wine3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "wine4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "wine5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "wine6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "wine7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "wine8", "화이트", "독일", 250000, "리슬링", 4.3f);
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<AdminWinePreviewResponse> adminWineResponsePageResponse = adminWineService.searchWinesAdmin("와인", null, null, null, pageable);

        // then
        assertWinePreviewPageResponse(adminWineResponsePageResponse, 0, 1, List.of(
                AdminWinePreviewResponse.of(wine1),
                AdminWinePreviewResponse.of(wine2),
                AdminWinePreviewResponse.of(wine3),
                AdminWinePreviewResponse.of(wine4),
                AdminWinePreviewResponse.of(wine5),
                AdminWinePreviewResponse.of(wine6),
                AdminWinePreviewResponse.of(wine7),
                AdminWinePreviewResponse.of(wine8)
        ));
    }

    @DisplayName("와인 종류로 와인 검색 테스트")
    @Test
    void searchWinesAdminBySort() {
        // Given
        Wine wine1 = createWine("와인1", "wine1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "wine2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "wine3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "wine4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "wine5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "wine6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "wine7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "wine8", "화이트", "독일", 250000, "리슬링", 4.3f);
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<AdminWinePreviewResponse> adminWineResponsePageResponse = adminWineService.searchWinesAdmin(null, "레드", null, null, pageable);

        // then
        assertWinePreviewPageResponse(adminWineResponsePageResponse, 0, 1, List.of(
                AdminWinePreviewResponse.of(wine1),
                AdminWinePreviewResponse.of(wine4),
                AdminWinePreviewResponse.of(wine7)
        ));
    }

    @DisplayName("와인 품종으로 와인 검색 테스트")
    @Test
    void searchWinesAdminByVariety() {
        // Given
        Wine wine1 = createWine("와인1", "wine1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "wine2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "wine3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "wine4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "wine5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "wine6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "wine7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "wine8", "화이트", "독일", 250000, "리슬링", 4.3f);
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<AdminWinePreviewResponse> adminWineResponsePageResponse = adminWineService.searchWinesAdmin(null, null, "피노누아", null, pageable);

        // then
        assertWinePreviewPageResponse(adminWineResponsePageResponse, 0, 1, List.of(
                AdminWinePreviewResponse.of(wine1),
                AdminWinePreviewResponse.of(wine3),
                AdminWinePreviewResponse.of(wine4),
                AdminWinePreviewResponse.of(wine6)
        ));
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
                .extracting("wineId", "name", "imageUrl", "sort", "country", "region", "createdAt")
                .containsExactly(
                        adminWinePreviewResponseList.stream()
                                .map(content -> tuple(content.getWineId(), content.getName(), content.getImageUrl(),
                                        content.getSort(), content.getCountry(), content.getRegion(), content.getCreatedAt()))
                                .toArray(Tuple[]::new)
                );
        Assertions.assertThat(adminWinePreviewResponsePageResponse)
                .extracting("pageNumber", "totalPages")
                .containsExactly(pageNumber, totalPages);
    }
}
