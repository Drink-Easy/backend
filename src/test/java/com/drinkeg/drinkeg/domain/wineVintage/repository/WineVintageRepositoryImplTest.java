package com.drinkeg.drinkeg.domain.wineVintage.repository;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class WineVintageRepositoryImplTest extends IntegrationTestSupport {

    @Autowired
    WineRepository wineRepository;
    @Autowired
    WineVintageRepository wineVintageRepository;

    @DisplayName("와인 아이디와 빈티지 연도로 와인 빈티지를 조회한다.")
    @Test
    void findByWineIdAndVintageYear() {
        // given
        Wine wine = wineRepository.save(createWine("와인 이름", "Wine Name", "레드", "프랑스", 15000, "카베르네 소비뇽", 4.5f));
        WineVintage wineVintage = wineVintageRepository.save(createWineVintage(wine, 2020));

        // when
        wineVintageRepository.findByWineIdAndVintageYear(wine.getId(), wineVintage.getVintageYear());

        // then
        assertAll(
                () -> assertThat(wineVintage.getWine().getId()).isEqualTo(wine.getId()),
                () -> assertThat(wineVintage.getVintageYear()).isEqualTo(2020)
        );
    }

    @DisplayName("와인 아이디와 빈티지 연도로 와인 빈티지를 조회할 때, 해당 빈티지가 없으면 null을 반환한다.")
    @Test
    void findByWineIdAndVintageYear_NotFound() {
        // given
        Wine wine = wineRepository.save(createWine("와인 이름", "Wine Name", "레드", "프랑스", 15000, "카베르네 소비뇽", 4.5f));

        // when
        WineVintage result = wineVintageRepository.findByWineIdAndVintageYear(wine.getId(), 2021);

        // then
        assertThat(result).isNull();
    }

    private Wine createWine(String name, String nameEng, String sort, String country, int price, String variety, float vivinoRating) {
        String cleanName = name.replaceAll("[ ,.'\\\\]", "").toLowerCase();
        String cleanNameEng = nameEng.replaceAll("[ ,.'\\\\]", "").toLowerCase();
        return Wine.builder()
                .name(cleanName)
                .nameEng(cleanNameEng)
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

    private WineVintage createWineVintage(Wine wine, Integer vintageYear) {
        return WineVintage.builder()
                .wine(wine)
                .vintageYear(vintageYear)
                .build();
    }


}
