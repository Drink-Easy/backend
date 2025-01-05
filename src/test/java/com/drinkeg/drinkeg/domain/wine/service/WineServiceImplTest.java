package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WineServiceImplTest extends IntegrationTestSupport {
    @Autowired
    WineRepository wineRepository;
    @Autowired
    WineService wineService;

    @DisplayName("와인 이름을 받아서 이름을 포함하는 모든 와인을 조회한다.")
    @Test
    void searchWineByName() {
        // given
        Wine wine1 = createWine("대중적인 레드 와인 10년");
        Wine wine2 = createWine("대중적인 화이트 와인 13년");
        Wine wine3 = createWine("대중적인 화이트 스파클링 와인 20년");
        Wine wine4 = createWine("매니아들이 찾는 레드 와인 30년");
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4));
        
        // when
        List<WinePreviewResponse> winePreviewList1 = wineService.searchWinesByName("0년");
        List<WinePreviewResponse> winePreviewList2 = wineService.searchWinesByName("대중적");
        // then
        assertThat(winePreviewList1).hasSize(3)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "대중적인 레드 와인 10년",
                        "대중적인 화이트 스파클링 와인 20년",
                        "매니아들이 찾는 레드 와인 30년"
                );

        assertThat(winePreviewList2).hasSize(3)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "대중적인 레드 와인 10년",
                        "대중적인 화이트 와인 13년",
                        "대중적인 화이트 스파클링 와인 20년"
                );
    }

    @DisplayName("존재하지 않는 와인 이름을 받으면 빈 리스트를 반환한다.")
    @Test
    void searchWineByNotExistingName() {
        // given
        Wine wine1 = createWine("대중적인 레드 와인 10년");
        Wine wine2 = createWine("대중적인 화이트 와인 13년");
        Wine wine3 = createWine("대중적인 화이트 스파클링 와인 20년");
        Wine wine4 = createWine("매니아들이 찾는 레드 와인 30년");
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4));
        // when
        List<WinePreviewResponse> winePreviewList = wineService.searchWinesByName("존재하지 않는 와인 이름으로 검색하기");
        // then
        assertThat(winePreviewList).isEmpty();
    }

    private Wine createWine(String name) {
        return Wine.builder()
                .name(name)
                .imageUrl("http://default.image")
                .sort("레드")
                .area("프랑스")
                .variety("샤도네이")
                .vivinoRating(4.1f)
                .price(100).build();
    }
}