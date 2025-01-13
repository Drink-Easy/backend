package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wine.repository.dto.WineNoteStatisticsAvgDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TastingNoteRepositoryImplTest extends IntegrationTestSupport {
    @Autowired
    TastingNoteRepository tastingNoteRepository;
    @Autowired
    WineRepository wineRepository;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("와인 아이디를 통해서 테이스팅 노트에 저장된 수치들의 평균치를 조회한다.")
    @Test
    void findAvgByWineId() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));


        TastingNote tastingNote1 = createTastingNote(member, wine,
                50, 30, 20, 40, 30, 10
        );

        TastingNote tastingNote2 = createTastingNote(member, wine,
                60, 35, 30, 40, 0, 4
        );

        TastingNote tastingNote3 = createTastingNote(member, wine,
                40, 40, 40, 40, 60, 7
        );

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3));
        // when
        WineNoteStatisticsAvgDto wineNoteStatisticsAvgDto = tastingNoteRepository.findWineNoteStatisticsByWineId(wine.getId());
        // then
        assertThat(wineNoteStatisticsAvgDto).extracting(
                        "avgSweetness", "avgAcidity", "avgTannin",
                        "avgBody", "avgAlcohol", "avgMemberRating")
                .containsExactly(50.0f, 35.0f, 30.0f, 40.0f, 30.0f, 7.0f);
    }

    @DisplayName("테이스팅 노트가 존재하지 않는 와인의 평균 수치값은 모두 0.0f로 나온다.")
    @Test
    void findAvgByWineIdWithNoTastingNote() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));
        // when
        WineNoteStatisticsAvgDto wineNoteStatisticsAvgDto = tastingNoteRepository.findWineNoteStatisticsByWineId(wine.getId());
        // then
        assertThat(wineNoteStatisticsAvgDto).extracting(
                        "avgSweetness", "avgAcidity", "avgTannin",
                        "avgBody", "avgAlcohol", "avgMemberRating")
                .containsExactly(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    @DisplayName("테이스팅 노트에서 가장 많이 선택된 nose 3개를 조회해서 반환한다.")
    @Test
    void findWineNoteStatisticsNoseByWineId() {
        // given
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));

        TastingNote tastingNote1 = createTastingNote(member, wine)
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2));

        TastingNote tastingNote2 = createTastingNote(member, wine)
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(1))
                .addNoseElement(noseList.get(2));

        TastingNote tastingNote3 = createTastingNote(member, wine)
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2))
                .addNoseElement(noseList.get(4));

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3));
        // when
        List<String> wineTopThreeNoses = tastingNoteRepository.findTopThreeNoseByWineId(wine.getId());
        // then
        assertThat(wineTopThreeNoses)
                .containsExactly("건포도", "오렌지", "시트러스");
    }

    @DisplayName("테이스팅 노트의 와인 nose가 3개 보다 적다면 존재하는 만큼만 반환한다.")
    @Test
    void findWineNoseStaticsNoseByWineIdLessThanThree() {
        // given
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));

        TastingNote tastingNote1 = createTastingNote(member, wine)
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2));

        tastingNoteRepository.save(tastingNote1);
        // when
        List<String> wineTopThreeNoses = tastingNoteRepository.findTopThreeNoseByWineId(wine.getId());
        // then
        assertThat(wineTopThreeNoses)
                .containsExactly("건포도", "오렌지");
    }

    @DisplayName("와인 아이디로 테이스팅 노트를 최신순 정렬 조회한다.")
    @Test
    void findTastingNoteByWineId() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));

        TastingNote tastingNote1 = createTastingNote(member, wine, 50, 30, 20, 40, 30, 10, "Review 1");
        TastingNote tastingNote2 = createTastingNote(member, wine, 60, 35, 30, 40, 0, 4, "Review 2");
        TastingNote tastingNote3 = createTastingNote(member, wine, 40, 40, 40, 40, 60, 7, "Review 3");

        tastingNoteRepository.save(tastingNote1);
        tastingNoteRepository.save(tastingNote2);
        tastingNoteRepository.save(tastingNote3);

        // when
        List<TastingNote> tastingNotes = tastingNoteRepository.findAllTastingNoteBy(wine.getId(), SortType.LATEST);

        // then
        assertThat(tastingNotes).hasSize(3)
                .extracting("review")
                .containsExactly("Review 3",
                        "Review 2",
                        "Review 1"
                );
    }

    @DisplayName("와인 아이디로 테이스팅 노트를 오래된 순 정렬 조회한다.")
    @Test
    void findTastingNoteByWineIdOldest() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));

        TastingNote tastingNote1 = createTastingNote(member, wine, 50, 30, 20, 40, 30, 10, "Review 1");
        TastingNote tastingNote2 = createTastingNote(member, wine, 60, 35, 30, 40, 0, 4, "Review 2");
        TastingNote tastingNote3 = createTastingNote(member, wine, 40, 40, 40, 40, 60, 7, "Review 3");

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3));

        // when
        List<TastingNote> tastingNotes = tastingNoteRepository.findAllTastingNoteBy(wine.getId(), SortType.OLDEST);

        // then
        assertThat(tastingNotes).hasSize(3)
                .extracting("review")
                .containsExactly(
                        "Review 1",
                        "Review 2",
                        "Review 3"
                );
    }

    @DisplayName("와인 아이디로 테이스팅 노트를 별점 높은순 정렬 조회한다.")
    @Test
    void findTastingNoteByWineIdHighestRating() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));

        TastingNote tastingNote1 = createTastingNote(member, wine, 50, 30, 20, 40, 30, 4.0f, "Review 1");
        TastingNote tastingNote2 = createTastingNote(member, wine, 60, 35, 30, 40, 0, 3.0f, "Review 2");
        TastingNote tastingNote3 = createTastingNote(member, wine, 40, 40, 40, 40, 60, 5.0f, "Review 3");

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3));

        // when
        List<TastingNote> tastingNotes = tastingNoteRepository.findAllTastingNoteBy(wine.getId(), SortType.HIGH_RATING);

        // then
        assertThat(tastingNotes).hasSize(3)
                .extracting("rating", "review")
                .containsExactly(
                        tuple(5.0f, "Review 3"),
                        tuple(4.0f, "Review 1"),
                        tuple(3.0f, "Review 2")
                );
    }

    @DisplayName("와인 아이디로 테이스팅 노트를 별점 낮은순 정렬 조회한다.")
    @Test
    void findTastingNoteByWineIdLowestRating() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));

        TastingNote tastingNote1 = createTastingNote(member, wine, 50, 30, 20, 40, 30, 2.0f, "Review 1");
        TastingNote tastingNote2 = createTastingNote(member, wine, 60, 35, 30, 40, 0, 1.0f, "Review 2");
        TastingNote tastingNote3 = createTastingNote(member, wine, 40, 40, 40, 40, 60, 3.0f, "Review 3");

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3));

        // when
        List<TastingNote> tastingNotes = tastingNoteRepository.findAllTastingNoteBy(wine.getId(), SortType.LOW_RATING);

        // then
        assertThat(tastingNotes).hasSize(3)
                .extracting("rating", "review")
                .containsExactly(
                        tuple(1.0f, "Review 2"),
                        tuple(2.0f, "Review 1"),
                        tuple(3.0f, "Review 3")
                );
    }

    @DisplayName("회원이 보유한 테이스팅 노트의 와인의 종류 수를 조회한다.")
    @Test
    void findTastingNoteSortCountsByUsername() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine1 = wineRepository.save(createWineWithSort("wine1", "레드"));
        Wine wine2 = wineRepository.save(createWineWithSort("wine2", "화이트"));
        Wine wine3 = wineRepository.save(createWineWithSort("wine3", "스파클링"));
        Wine wine4 = wineRepository.save(createWineWithSort("wine4", "로제"));
        Wine wine5 = wineRepository.save(createWineWithSort("wine5", "기타"));
        Wine wine6 = wineRepository.save(createWineWithSort("wine6", "기타"));


        TastingNote tastingNote1 = createTastingNote(member, wine1);
        TastingNote tastingNote2 = createTastingNote(member, wine2);
        TastingNote tastingNote3 = createTastingNote(member, wine3);
        TastingNote tastingNote4 = createTastingNote(member, wine4);
        TastingNote tastingNote5 = createTastingNote(member, wine5);
        TastingNote tastingNote6 = createTastingNote(member, wine6);

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3, tastingNote4, tastingNote5, tastingNote6));

        // when
        TastingNoteSortCountResponse tastingNoteSortCountsByUsername = tastingNoteRepository.findTastingNoteSortCountsByUsername(member.getUsername());

        // then
        assertThat(tastingNoteSortCountsByUsername).extracting(
                        "totalCount", "redCount", "whiteCount",
                        "sparklingCount", "roseCount", "etcCount")
                .containsExactly(6, 1, 1, 1, 1, 2);
    }

    @DisplayName("회원의 테이스팅 노트가 없는 경우 모든 와인의 종류 수는 0이다.")
    @Test
    void findTastingNoteSortCountsByUsernameWithNoTastingNote() {
        // given
        Member member = memberRepository.save(createMember("user"));

        // when
        TastingNoteSortCountResponse tastingNoteSortCountsByUsername = tastingNoteRepository.findTastingNoteSortCountsByUsername(member.getUsername());

        // then
        assertThat(tastingNoteSortCountsByUsername).extracting(
                        "totalCount", "redCount", "whiteCount",
                        "sparklingCount", "roseCount", "etcCount")
                .containsExactly(0, 0, 0, 0, 0, 0);
    }

    @DisplayName("회원이 보유한 테이스트를 와인 종류별로 조회한다.")
    @Test
    void findTastingNoteBySortAndUsernameAll() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine1 = wineRepository.save(createWineWithSort("wine1", "레드"));
        Wine wine2 = wineRepository.save(createWineWithSort("wine2", "화이트"));
        Wine wine3 = wineRepository.save(createWineWithSort("wine3", "스파클링"));
        Wine wine4 = wineRepository.save(createWineWithSort("wine4", "로제"));
        Wine wine5 = wineRepository.save(createWineWithSort("wine5", "주정강화"));
        Wine wine6 = wineRepository.save(createWineWithSort("wine6", "기타"));

        TastingNote tastingNote1 = createTastingNote(member, wine1);
        TastingNote tastingNote2 = createTastingNote(member, wine2);
        TastingNote tastingNote3 = createTastingNote(member, wine3);
        TastingNote tastingNote4 = createTastingNote(member, wine4);
        TastingNote tastingNote5 = createTastingNote(member, wine5);
        TastingNote tastingNote6 = createTastingNote(member, wine6);

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3, tastingNote4, tastingNote5, tastingNote6));

        // when
        List<TastingNote> tastingNotes = tastingNoteRepository.findTastingNoteBySortAndUsername(TastingNoteWineSort.ALL, member.getUsername());

        // then
        assertThat(tastingNotes).hasSize(6)
                .extracting("wine.sort")
                .containsExactly("기타", "주정강화", "로제", "스파클링", "화이트", "레드");
    }

    @DisplayName("회원이 보유한 테이스트를 와인 종류별로 조회한다.")
    @Test
    void findTastingNoteBySortAndUsernameRed() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine1 = wineRepository.save(createWineWithSort("wine1", "레드"));
        Wine wine2 = wineRepository.save(createWineWithSort("wine2", "화이트"));
        Wine wine3 = wineRepository.save(createWineWithSort("wine3", "스파클링"));
        Wine wine4 = wineRepository.save(createWineWithSort("wine4", "로제"));
        Wine wine5 = wineRepository.save(createWineWithSort("wine5", "기타"));
        Wine wine6 = wineRepository.save(createWineWithSort("wine6", "기타"));

        TastingNote tastingNote1 = createTastingNote(member, wine1);
        TastingNote tastingNote2 = createTastingNote(member, wine2);
        TastingNote tastingNote3 = createTastingNote(member, wine3);
        TastingNote tastingNote4 = createTastingNote(member, wine4);
        TastingNote tastingNote5 = createTastingNote(member, wine5);
        TastingNote tastingNote6 = createTastingNote(member, wine6);

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3, tastingNote4, tastingNote5, tastingNote6));

        // when
        List<TastingNote> tastingNotes = tastingNoteRepository.findTastingNoteBySortAndUsername(TastingNoteWineSort.RED, member.getUsername());

        // then
        assertThat(tastingNotes).hasSize(1)
                .extracting("wine.sort")
                .containsExactly("레드");
    }
    @DisplayName("회원이 보유한 테이스트를 와인 종류별로 조회한다.")
    @Test
    void findTastingNoteBySortAndUsernameEtc() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine1 = wineRepository.save(createWineWithSort("wine1", "레드"));
        Wine wine2 = wineRepository.save(createWineWithSort("wine2", "화이트"));
        Wine wine3 = wineRepository.save(createWineWithSort("wine3", "스파클링"));
        Wine wine4 = wineRepository.save(createWineWithSort("wine4", "로제"));
        Wine wine5 = wineRepository.save(createWineWithSort("wine5", "주정강화"));
        Wine wine6 = wineRepository.save(createWineWithSort("wine6", "기타"));

        TastingNote tastingNote1 = createTastingNote(member, wine1);
        TastingNote tastingNote2 = createTastingNote(member, wine2);
        TastingNote tastingNote3 = createTastingNote(member, wine3);
        TastingNote tastingNote4 = createTastingNote(member, wine4);
        TastingNote tastingNote5 = createTastingNote(member, wine5);
        TastingNote tastingNote6 = createTastingNote(member, wine6);

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3, tastingNote4, tastingNote5, tastingNote6));

        // when
        List<TastingNote> tastingNotes = tastingNoteRepository.findTastingNoteBySortAndUsername(TastingNoteWineSort.ETCETERA, member.getUsername());

        // then
        assertThat(tastingNotes).hasSize(2)
                .extracting("wine.sort")
                .containsExactly("기타", "주정강화");
    }

    private TastingNote createTastingNote(Member member, Wine wine,
                                          int sweetness, int acidity, int tannin, int body, int alcohol,
                                          float rating, String review) {
        return TastingNote.builder()
                .member(member)
                .wine(wine)
                .color("빨간색")
                .tasteDate(LocalDate.of(2025, 1, 6))
                .sweetness(sweetness)
                .acidity(acidity)
                .tannin(tannin)
                .body(body)
                .alcohol(alcohol)
                .rating(rating)
                .review(review).build();
    }

    private TastingNote createTastingNote(Member member, Wine wine,
                                          int sweetness, int acidity, int tannin, int body, int alcohol,
                                          float rating) {
        return TastingNote.builder()
                .member(member)
                .wine(wine)
                .color("빨간색")
                .tasteDate(LocalDate.of(2025, 1, 6))
                .sweetness(sweetness)
                .acidity(acidity)
                .tannin(tannin)
                .body(body)
                .alcohol(alcohol)
                .rating(rating)
                .review("나쁘지 않아요").build();
    }

    private TastingNote createTastingNote(Member member, Wine wine) {
        return createTastingNote(member, wine, 0, 0, 0, 0, 0, 0);
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

    private Wine createWineWithSort(String name, String sort) {
        return Wine.builder()
                .name(name)
                .imageUrl("http://default.image")
                .sort(sort)
                .country("프랑스")
                .region("보르도")
                .variety("샤도네이")
                .vivinoRating(4.1f)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(100).build();
    }

    private Member createMember(String username) {
        return Member.builder()
                .username(username)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .build();
    }
}