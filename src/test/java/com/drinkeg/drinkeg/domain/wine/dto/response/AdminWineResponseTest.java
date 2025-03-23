package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AdminWineResponseTest  extends IntegrationTestSupport {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineRepository wineRepository;
    @Autowired
    TastingNoteRepository tastingNoteRepository;

    @DisplayName("AdminWineResponse of 메서드에 매개변수로 Wine가 들어가면 AdminWineResponse로 변환한다.")
    @Test
    void AdminWineResponseOf() {
        // given
        Long wineId = wineRepository.save(createWine("와인")).getId();

        // when
        Wine wine = wineRepository.findById(wineId).get();
        AdminWineResponse adminWineResponse = AdminWineResponse.of(wine);

        // then
        assertThat(adminWineResponse.getAdminWinePreviewResponse())
                .extracting("wineId", "name", "sort", "variety", "country", "region", "createdAt")
                .containsExactly(wineId, "와인", "레드", "샤도네이", "프랑스", "보르도", wine.getCreatedAt());
        assertThat(adminWineResponse.getAdminWineDetailResponse())
                .extracting("wineId", "name", "imageUrl", "sort", "country", "region", "variety", "vivinoRating", "price")
                .containsExactly(wineId, "와인", "http://default.image", "레드", "프랑스", "보르도", "샤도네이", 4.1f, 100);
    }

    @DisplayName("AdminWinePreviewResponse of 메서드에 매개변수로 Wine가 들어가면 AdminWinePreviewResponse로 변환한다.")
    @Test
    void AdminWinePreviewResponseOf() {
        // given
        Long wineId = wineRepository.save(createWine("와인")).getId();

        // when
        Wine wine = wineRepository.findById(wineId).get();
        AdminWinePreviewResponse adminWinePreviewResponse = AdminWinePreviewResponse.of(wine);

        // then
        assertThat(adminWinePreviewResponse)
                .extracting("wineId", "name", "sort", "variety", "country", "region", "createdAt")
                .containsExactly(wineId, "와인", "레드", "샤도네이", "프랑스", "보르도", wine.getCreatedAt());
    }

    @DisplayName("AdminWineDetailResponse of 메서드에 매개변수로 Wine가 들어가면 AdminWineDetailResponse로 변환한다.")
    @Test
    void AdminWineDetailResponseOf() {
        // given
        Long wineId = wineRepository.save(createWine("와인")).getId();

        // when
        Wine wine = wineRepository.findById(wineId).get();
        AdminWineDetailResponse adminWineDetailResponse = AdminWineDetailResponse.of(wine);

        // then
        assertThat(adminWineDetailResponse)
                .extracting("wineId", "name", "imageUrl", "sort", "country", "region", "variety", "vivinoRating", "price")
                .containsExactly(wineId, "와인", "http://default.image", "레드", "프랑스", "보르도", "샤도네이", 4.1f, 100);

    }

    private Wine createWine(String name) {
        return Wine.builder()
                .name(name)
                .imageUrl("http://default.image")
                .sort("레드")
                .country("프랑스")
                .region("보르도")
                .variety("샤도네이")
                .vivinoRating(4.1f)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(100).build();
    }
}
