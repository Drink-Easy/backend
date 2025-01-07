package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class WineServiceImplTest extends IntegrationTestSupport {
    @Autowired
    WineRepository wineRepository;
    @Autowired
    WineService wineService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TastingNoteRepository tastingNoteRepository;
    @Autowired
    WineWishlistRepository wineWishlistRepository;

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

    @DisplayName("와인 아이디를 받아서 와인의 통계 정보를 업데이트 한다.")
    @Test
    void updateWineStatisticsByWineId() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");

        TastingNote tastingNote1 = createTastingNote(member, wine,
                50, 30, 20, 40, 30, 10)
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2));
        TastingNote tastingNote2 = createTastingNote(member, wine,
                60, 35, 30, 40, 0, 4)
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(1))
                .addNoseElement(noseList.get(2));
        TastingNote tastingNote3 = createTastingNote(member, wine,
                40, 40, 40, 40, 60, 7)
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2))
                .addNoseElement(noseList.get(4));

        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3));
        // when
        wineService.updateWineNoteStatics(wine.getId());
        // then
        assertThat(wine.getWineNoteStatistics())
                .extracting(
                        "avgSugarContent", "avgAcidity", "avgTannin", "avgBody", "avgAlcohol", "avgMemberRating",
                        "nose1", "nose2", "nose3")
                .containsExactly(
                        50.0f, 35.0f, 30.0f, 40.0f, 30.0f, 7.0f,
                        "건포도", "오렌지", "시트러스");
    }

    @DisplayName("와인 아이디를 받아서 와인의 상세 정보를 최근 리뷰 3개와 함꼐 반환한다.")
    @Test
    void findWineInfoWithThreeLatestReviews() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");
        wineWishlistRepository.save(WineWishlist.create(member, wine));

        TastingNote tastingNote1 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 0, "가성비 좋아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2));
        TastingNote tastingNote2 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 5, "나쁘지 않아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(1))
                .addNoseElement(noseList.get(2));
        TastingNote tastingNote3 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 10, "맛있어요!")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2))
                .addNoseElement(noseList.get(4));
        TastingNote tastingNote4 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 10, "고기랑 먹기 좋아요!");
        TastingNote tastingNote5 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 10, "다시 구매할 것 같아요");
        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3, tastingNote4, tastingNote5));
        wineService.updateWineNoteStatics(wine.getId());
        // when
        WineWithThreeReviewsResponse wineInfo = wineService.getWineInfoWithThreeReviews(wine.getId(), member.getUsername());
        // then
        assertThat(wineInfo.getWineInfoResponse())
                .extracting(
                        "wineId", "name", "imageUrl", "price", "sort", "area", "variety", "vivinoRating",
                        "avgSugarContent", "avgAcidity", "avgTannin", "avgBody", "avgAlcohol",
                        "nose1", "nose2", "nose3", "avgMemberRating", "liked"
                )
                .containsExactly(wine.getId(), wine.getName(), wine.getImageUrl(), wine.getPrice(), wine.getSort(),
                        wine.getArea(), wine.getVariety(), wine.getVivinoRating(),
                        50.0f, 30.0f, 20.0f, 40.0f, 30.0f,
                        "건포도", "오렌지", "시트러스", 7.0f, true
                );

        assertThat(wineInfo.getRecentReviews())
                .extracting("review", "rating")
                .containsExactly(
                        tuple("다시 구매할 것 같아요", 10.0f),
                        tuple("고기랑 먹기 좋아요!", 10.0f),
                        tuple("맛있어요!", 10.0f)
                );
    }

    @DisplayName("와인 리뷰가 존재하지 않는 경우 리뷰가 반환되지 않는다.")
    @Test
    void findWineInfoWithNoLatestReviews() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");
        wineWishlistRepository.save(WineWishlist.create(member, wine));

        wineService.updateWineNoteStatics(wine.getId());
        // when
        WineWithThreeReviewsResponse wineInfo = wineService.getWineInfoWithThreeReviews(wine.getId(), member.getUsername());
        // then
        assertThat(wineInfo.getWineInfoResponse())
                .extracting(
                        "wineId", "name", "imageUrl", "price", "sort", "area", "variety", "vivinoRating",
                        "avgSugarContent", "avgAcidity", "avgTannin", "avgBody", "avgAlcohol",
                        "nose1", "nose2", "nose3", "avgMemberRating", "liked"
                )
                .containsExactly(wine.getId(), wine.getName(), wine.getImageUrl(), wine.getPrice(), wine.getSort(),
                        wine.getArea(), wine.getVariety(), wine.getVivinoRating(),
                        0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                        null, null, null, 0.0f, true
                );

        assertThat(wineInfo.getRecentReviews()).isEmpty();
    }

    @DisplayName("와인 아이디로 와인 리뷰를 전체 조회한다. 정렬 순서는 최신순이다.")
    @Test
    void findWineReviewByWineId() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("레드 와인"));
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");

        TastingNote tastingNote1 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 0, "가성비 좋아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2));
        TastingNote tastingNote2 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 5, "나쁘지 않아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(1))
                .addNoseElement(noseList.get(2));
        TastingNote tastingNote3 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 10, "맛있어요!")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2))
                .addNoseElement(noseList.get(4));
        TastingNote tastingNote4 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 10, "고기랑 먹기 좋아요!");
        TastingNote tastingNote5 = createTastingNote(member, wine, "빨간색",
                50, 30, 20, 40, 30, 10, "다시 구매할 것 같아요");
        tastingNoteRepository.saveAll(List.of(tastingNote1, tastingNote2, tastingNote3, tastingNote4, tastingNote5));
        // when
        List<WineReviewResponse> wineReviews = wineService.getWineReviewsAndIsLikedByWineId(wine.getId(), SortType.LATEST);
        // then
        assertThat(wineReviews).hasSize(5)
                .extracting("review", "rating")
                .containsExactly(
                        tuple("다시 구매할 것 같아요", 10.0f),
                        tuple("고기랑 먹기 좋아요!", 10.0f),
                        tuple("맛있어요!", 10.0f),
                        tuple("나쁘지 않아요", 5.0f),
                        tuple("가성비 좋아요", 0.0f)
                );
    }

    @DisplayName("잘못된 와인 아이디로 와인 리뷰를 전체 조회하면 예외가 발생한다.")
    @Test
    void findWineReviewByWrongWineId() {
        // given // when // then
        assertThatThrownBy(() -> wineService.getWineReviewsAndIsLikedByWineId(-1L, SortType.LATEST))
                .isInstanceOf(GeneralException.class)
                .hasMessage("와인이 없습니다.");
    }

    // 편의 메서드

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

    private TastingNote createTastingNote(Member member, Wine wine,
                                          int sugarContent, int acidity, int tannin, int body, int alcohol,
                                          float rating) {
        return createTastingNote(member, wine, "빨간색",
                sugarContent, acidity, tannin, body, alcohol,
                rating, "나쁘지 않아요");
    }



    private TastingNote createTastingNote(Member member, Wine wine) {
        return createTastingNote(member, wine, 0, 0, 0, 0, 0, 0);
    }

    private TastingNote createTastingNote(Member member, Wine wine, String color,
                                          int sugarContent, int acidity, int tannin, int body, int alcohol,
                                          float rating, String review) {
        return TastingNote.builder()
                .member(member)
                .wine(wine)
                .color(color)
                .tasteDate(LocalDate.of(2025, 1, 6))
                .sugarContent(sugarContent)
                .acidity(acidity)
                .tannin(tannin)
                .body(body)
                .alcohol(alcohol)
                .rating(rating)
                .review(review).build();
    }

    private Member createMember(String username) {
        return Member.builder()
                .username(username)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .build();
    }
}