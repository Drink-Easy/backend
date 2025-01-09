package com.drinkeg.drinkeg.domain.wine.domain;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class WineNoteStatisticsTest extends IntegrationTestSupport {
    @DisplayName("노즈 리스트가 들어오면 모두 저장한다.")
    @Test
    void updateWineNoteStatisticsNose() {
        // given
        List<String> noseList = List.of("오렌지", "아몬드", "가죽");
        WineNoteStatistics wineNoteStatistics = new WineNoteStatistics();
        // when
        wineNoteStatistics.updateNose(noseList);
        // then
        assertThat(wineNoteStatistics.getNose1()).isEqualTo("오렌지");
        assertThat(wineNoteStatistics.getNose2()).isEqualTo("아몬드");
        assertThat(wineNoteStatistics.getNose3()).isEqualTo("가죽");
    }

    @DisplayName("노즈 리스트가 들어오면 들어온 원소 만큼만 저장한다.")
    @Test
    void updateWineNoteStatisticsNoseWithSize1NoseList() {
        // given
        List<String> noseList = List.of("오렌지");
        WineNoteStatistics wineNoteStatistics = new WineNoteStatistics();
        // when
        wineNoteStatistics.updateNose(noseList);
        // then
        assertThat(wineNoteStatistics.getNose1()).isEqualTo("오렌지");
        assertThat(wineNoteStatistics.getNose2()).isNull();
        assertThat(wineNoteStatistics.getNose3()).isNull();
    }
}