package com.drinkeg.drinkeg.domain.wineVintage.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wineVintage.repository.WineVintageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.drinkeg.drinkeg.domain.member.domain.Member.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class WineVintageServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private WineVintageService wineVintageService;
    @Autowired
    private WineVintageRepository wineVintageRepository;
    @Autowired
    private WineRepository wineRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TastingNoteRepository tastingNoteRepository;

    @DisplayName("와인 빈티지 아이디로 와인 빈티지 노트 통계를 업데이트한다.")
    @Test
    void updateWineVintageNoteStatics() {
        // given
        Wine wine = wineRepository.save(createWine("와인 이름", "Wine Name", "레드", "프랑스", 15000, "카베르네 소비뇽", 4.5f));
        WineVintage wineVintage = wineVintageRepository.save(createWineVintage(wine, 2020));
        Member member = memberRepository.save(createMember("testUser", "testPassword", true));
        TastingNote tastingNote1 = tastingNoteRepository.save(TastingNote.create(member, wineVintage, createTastingNoteRequestDetail(
                wine.getId(), wineVintage.getVintageYear(), "레드", LocalDate.now(),
                50, 50, 50, 50, 50, List.of("과일향"), 4.0f, "첫 번째 노트")));
        TastingNote tastingNote2 = tastingNoteRepository.save(TastingNote.create(member, wineVintage, createTastingNoteRequestDetail(
                wine.getId(), wineVintage.getVintageYear(), "레드", LocalDate.now(),
                60, 60, 60, 60, 60, List.of("향신료향"), 4.5f, "두 번째 노트")));
        TastingNote tastingNote3 = tastingNoteRepository.save(TastingNote.create(member, wineVintage, createTastingNoteRequestDetail(
                wine.getId(), wineVintage.getVintageYear(), "레드", LocalDate.now(),
                70, 70, 70, 70, 70, List.of("나무향"), 5.0f, "세 번째 노트")));


        // when
        wineVintageService.updateWineVintageNoteStatics(wineVintage.getId());

        // then
        WineVintage updatedWineVintage = wineVintageRepository.findById(wineVintage.getId())
                .orElseThrow(() -> new IllegalArgumentException("와인 빈티지를 찾을 수 없습니다."));

        assertAll(
                () -> assertThat(updatedWineVintage.getWineNoteStatistics().getAvgMemberRating()).isEqualTo(4.5f),
                () -> assertThat(updatedWineVintage.getWineNoteStatistics().getAvgSweetness()).isEqualTo(60),
                () -> assertThat(updatedWineVintage.getWineNoteStatistics().getAvgAcidity()).isEqualTo(60),
                () -> assertThat(updatedWineVintage.getWineNoteStatistics().getAvgTannin()).isEqualTo(60),
                () -> assertThat(updatedWineVintage.getWineNoteStatistics().getAvgBody()).isEqualTo(60),
                () -> assertThat(updatedWineVintage.getWineNoteStatistics().getAvgAlcohol()).isEqualTo(60)
        );
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

    private TastingNoteRequest createTastingNoteRequestDetail(Long wineId, Integer wineVintage, String color, LocalDate tasteDate,
                                                              Integer sweetness, Integer acidity, Integer tannin, Integer body, Integer alcohol,
                                                              List<String> noseList, Float rating, String review) {
        return new TastingNoteRequest(wineId, wineVintage, color, tasteDate, sweetness, acidity, tannin, body, alcohol, noseList, rating, review);
    }
}
