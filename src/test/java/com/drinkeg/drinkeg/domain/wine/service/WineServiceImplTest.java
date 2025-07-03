package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wineVintage.repository.WineVintageRepository;
import com.drinkeg.drinkeg.domain.wineVintage.service.WineVintageService;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class WineServiceImplTest extends IntegrationTestSupport {
    @Autowired
    WineRepository wineRepository;
    @Autowired
    WineService wineService;
    @Autowired
    WineVintageService wineVintageService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TastingNoteRepository tastingNoteRepository;
    @Autowired
    WineWishlistRepository wineWishlistRepository;
    @Autowired
    WineVintageRepository wineVintageRepository;

    @DisplayName("와인 이름을 받아서 이름을 포함하는 모든 와인을 조회한다.")
    @Test
    void searchWineByName() {
        // given
        Wine wine1 = saveWine("대중적인 레드 와인 10년", "wine popular red 10 years", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = saveWine("대중적인 화이트 와인 13년", "wine popular white 13 years", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = saveWine("대중적인 화이트 스파클링 와인 20년", "wine popular white sparkling 20 years", "스파클링", "스페인", 30000, "샴페인", 4.2f);
        Wine wine4 = saveWine("매니아들이 찾는 레드 와인 30년", "wine red that manias find 30 years", "레드", "미국", 40000, "메를로", 4.8f);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<WinePreviewResponse> winePreviewResponsePageResponse1 = wineService.searchWinesByName("0년", pageable);
        PageResponse<WinePreviewResponse> winePreviewResponsePageResponse2 = wineService.searchWinesByName("대중적", pageable);

        // then
        assertWinePreviewPageResponse(winePreviewResponsePageResponse1, 0, 1,
                List.of(WinePreviewResponse.of(wine1), WinePreviewResponse.of(wine3), WinePreviewResponse.of(wine4)));
        assertWinePreviewPageResponse(winePreviewResponsePageResponse2, 0, 1,
                List.of(WinePreviewResponse.of(wine1), WinePreviewResponse.of(wine3), WinePreviewResponse.of(wine2)));
    }

    @DisplayName("와인 이름을 영어로 받으면 영어로 된 와인 이름을 포함하는 모든 와인을 조회한다.")
    @Test
    void searchWineByNameEng() {
        // given
        Wine wine1 = saveWine("대중적인 레드 와인 10년", "popular red wine 10 years", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = saveWine("대중적인 화이트 와인 13년", "popular white wine 13 years", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = saveWine("대중적인 화이트 스파클링 와인 20년", "popular white sparkling wine 20 years", "스파클링", "스페인", 30000, "샴페인", 4.2f);
        Wine wine4 = saveWine("매니아들이 찾는 레드 와인 30년", "red wine that manias find 30 years", "레드", "미국", 40000, "메를로", 4.8f);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<WinePreviewResponse> winePreviewResponsePageResponse1 = wineService.searchWinesByName("0 years", pageable);
        PageResponse<WinePreviewResponse> winePreviewResponsePageResponse2 = wineService.searchWinesByName("popular", pageable);

        // then
        assertWinePreviewPageResponse(winePreviewResponsePageResponse1, 0, 1,
                List.of(WinePreviewResponse.of(wine1), WinePreviewResponse.of(wine3), WinePreviewResponse.of(wine4)));

        assertWinePreviewPageResponse(winePreviewResponsePageResponse2, 0, 1,
                List.of(WinePreviewResponse.of(wine1), WinePreviewResponse.of(wine3), WinePreviewResponse.of(wine2)));
    }

    @DisplayName("존재하지 않는 와인 이름을 받으면 빈 리스트를 반환한다.")
    @Test
    void searchWineByNotExistingName() {
        // given
        saveWine("대중적인 레드 와인 10년", "popular red wine 10 years", "레드", "프랑스", 10000, "피노누아", 4.5f);
        saveWine("대중적인 화이트 와인 13년", "popular white wine 13 years", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        saveWine("대중적인 화이트 스파클링 와인 20년", "popular white sparkling wine 20 years", "스파클링", "스페인", 30000, "샴페인", 4.2f);
        saveWine("매니아들이 찾는 레드 와인 30년", "red wine that manias find 30 years", "레드", "미국", 40000, "메를로", 4.8f);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<WinePreviewResponse> winePreviewResponsePageResponse = wineService.searchWinesByName("존재하지 않는 와인 이름으로 검색하기", pageable);

        // then
        assertWinePreviewPageResponse(winePreviewResponsePageResponse, 0, 0, new ArrayList<>());
    }

    @DisplayName("와인 아이디를 받아서 와인의 통계 정보를 업데이트 한다.")
    @Test
    void updateWineStatisticsByWineId() {
        // given
        Member member = saveMember("user", List.of("레드", "화이트"), List.of("프랑스", "이탈리아"), 60000L);
        Wine wine = saveWine("레드 와인", "Red Wine", "레드", "프랑스", 15000, "메를로", 4.5f);
        WineVintage wineVintage = saveWineVintage(wine, 2017);
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");

        saveTastingNote(member, wineVintage, "FFFFFF",
                50, 30, 20, 40, 30, 10, "가성비 좋아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2));
        saveTastingNote(member, wineVintage, "FFFFFF",
                60, 35, 30, 40, 0, 4, "나쁘지 않아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(1))
                .addNoseElement(noseList.get(2));
        saveTastingNote(member, wineVintage,"FFFFFF",
                40, 40, 40, 40, 60, 7, "맛있어요!")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2))
                .addNoseElement(noseList.get(4));

        // when
        wineService.updateWineNoteStatics(wine.getId());
        // then
        assertThat(wine.getWineNoteStatistics())
                .extracting(
                        "avgSweetness", "avgAcidity", "avgTannin", "avgBody", "avgAlcohol", "avgMemberRating",
                        "nose1", "nose2", "nose3")
                .containsExactly(
                        50.0f, 35.0f, 30.0f, 40.0f, 30.0f, 7.0f,
                        "건포도", "오렌지", "시트러스");
    }

    @DisplayName("와인 아이디와 빈티지 년도를 받아서 와인의 상세 정보를 최근 리뷰 3개와 함꼐 반환한다.")
    @Test
    void findWineInfoWithThreeLatestReviews() {
        // given
        Member member = saveMember("user", List.of("레드", "화이트"), List.of("프랑스", "이탈리아"), 60000L);
        Wine wine = saveWine("레드 와인", "Red Wine", "레드", "프랑스", 15000, "메를로", 4.5f);
        WineVintage wineVintage = saveWineVintage(wine, 2017);
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");
        wineWishlistRepository.save(WineWishlist.create(member, wineVintage));

        saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 0, "가성비 좋아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2));
        saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 5, "나쁘지 않아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(1))
                .addNoseElement(noseList.get(2));
        saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 10, "맛있어요!")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2))
                .addNoseElement(noseList.get(4));
        saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 10, "고기랑 먹기 좋아요!");
        saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 10, "다시 구매할 것 같아요");
        wineService.updateWineNoteStatics(wine.getId());
        wineVintageService.updateWineVintageNoteStatics(wineVintage.getId());

        // when
        WineWithThreeReviewsResponse wineInfo = wineService.getWineInfoWithThreeReviews(wine.getId(), wineVintage.getVintageYear(), member.getUsername());

        // then
        assertThat(wineInfo.getWineInfoResponse())
                .extracting(
                        "wineId", "name", "vintageYear", "imageUrl", "price", "sort", "country", "variety", "vivinoRating",
                        "avgSweetness", "avgAcidity", "avgTannin", "avgBody", "avgAlcohol",
                        "nose1", "nose2", "nose3", "avgMemberRating", "liked"
                )
                .containsExactly(wine.getId(), wine.getName(), wineVintage.getVintageYear(), wine.getImageUrl(), wine.getPrice(), wine.getSort(),
                        wine.getCountry(), wine.getVariety(), wine.getVivinoRating(),
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
        Member member = saveMember("user", List.of("레드", "화이트"), List.of("프랑스", "이탈리아"), 60000L);
        Wine wine = saveWine("레드 와인", "Red Wine", "레드", "프랑스", 15000, "메를로", 4.5f);
        WineVintage wineVintage = saveWineVintage(wine, 2017);
        wineWishlistRepository.save(WineWishlist.create(member, wineVintage));

        // when
        WineWithThreeReviewsResponse wineInfo = wineService.getWineInfoWithThreeReviews(wine.getId(), 2017, member.getUsername());
        // then
        assertThat(wineInfo.getWineInfoResponse())
                .extracting(
                        "wineId", "name", "imageUrl", "price", "sort", "country", "variety", "vivinoRating",
                        "avgSweetness", "avgAcidity", "avgTannin", "avgBody", "avgAlcohol",
                        "nose1", "nose2", "nose3", "avgMemberRating", "liked"
                )
                .containsExactly(wine.getId(), wine.getName(), wine.getImageUrl(), wine.getPrice(), wine.getSort(),
                        wine.getCountry(), wine.getVariety(), wine.getVivinoRating(),
                        0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                        null, null, null, 0.0f, true
                );

        assertThat(wineInfo.getRecentReviews()).isEmpty();
    }

    @DisplayName("와인 아이디와 빈티지로 와인 리뷰를 전체 조회한다. 정렬 순서는 최신순이다.")
    @Test
    void findWineReviewByWineId() {
        // given
        Member member = saveMember("user", List.of("레드", "화이트"), List.of("프랑스", "이탈리아"), 60000L);
        Wine wine = saveWine("레드 와인", "Red Wine", "레드", "프랑스", 15000, "메를로", 4.5f);
        WineVintage wineVintage = saveWineVintage(wine, 2017);
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");

        TastingNote tastingNote1 = saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 0, "가성비 좋아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2));
        TastingNote tastingNote2 = saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 5, "나쁘지 않아요")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(1))
                .addNoseElement(noseList.get(2));
        TastingNote tastingNote3 = saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 10, "맛있어요!")
                .addNoseElement(noseList.get(0))
                .addNoseElement(noseList.get(2))
                .addNoseElement(noseList.get(4));
        TastingNote tastingNote4 = saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 10, "고기랑 먹기 좋아요!");
        TastingNote tastingNote5 = saveTastingNote(member, wineVintage, "빨간색",
                50, 30, 20, 40, 30, 10, "다시 구매할 것 같아요");

        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<WineReviewResponse> wineReviewsAndIsLikedPage = wineService.getWineReviewsByWineIdAndVintageYear(wine.getId(), 2017 ,SortType.LATEST, pageable);

        // then
        assertWineReviewPageResponse(wineReviewsAndIsLikedPage, 0, 1,
                List.of(WineReviewResponse.of(tastingNote5), WineReviewResponse.of(tastingNote4), WineReviewResponse.of(tastingNote3),
                        WineReviewResponse.of(tastingNote2), WineReviewResponse.of(tastingNote1)));
    }

    @DisplayName("와인 아이디로 와인 리뷰를 전체 조회한다. 리뷰가 없는 경우 content가 비어있다.")
    @Test
    void findWineReviewByWineIdWithNoReview() {
        // given
        Wine wine = saveWine("레드 와인", "Red Wine", "레드", "프랑스", 15000, "메를로", 4.5f);
        saveWineVintage(wine, 2017);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageResponse<WineReviewResponse> wineReviewsAndIsLikedPage = wineService.getWineReviewsByWineIdAndVintageYear(wine.getId(), 2017, SortType.LATEST, pageable);

        // then
        assertWineReviewPageResponse(wineReviewsAndIsLikedPage, 0, 0, new ArrayList<>());
    }

    @DisplayName("잘못된 와인 아이디로 와인 리뷰를 전체 조회하면 예외가 발생한다.")
    @Test
    void findWineReviewByWrongWineId() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        // when // then
        assertThatThrownBy(() -> wineService.getWineReviewsByWineIdAndVintageYear(-1L, 2017, SortType.LATEST, pageable))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.WINE_VINTAGE_NOT_FOUND.getMessage());
    }

    @DisplayName("사용자의 취향으로 추천 와인을 반환한다.")
    @Test
    void findRecommendWineByMemberPreferences() {
        // given
        Member member = memberRepository.save(saveMember("user", List.of("레드", "화이트"), List.of("프랑스", "이탈리아"), 60000L));
        saveWine("와인1", "wine1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        saveWine("와인2", "wine2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        saveWine("와인3", "wine3", "로제", "스페인", 30000, "피노누아", 3.5f);
        saveWine("와인4", "wine4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        saveWine("와인5", "wine5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        saveWine("와인6", "wine6", "로제", "스페인", 60000, "피노누아", 3.5f);
        saveWine("와인7", "wine7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f);
        saveWine("와인8", "wine8", "화이트", "독일", 250000, "리슬링", 4.3f);
        // when
        List<HomeWineResponse> recommendWineList = wineService.getRecommendWineList(member.getUsername());
        // then
        assertThat(recommendWineList).hasSize(2)
                .extracting("wineName")
                .containsExactlyInAnyOrder("와인1", "와인4");
    }

    @DisplayName("가장 인기있는 와인 10개를 반환한다.")
    @Test
    void findMostLikedWine() {
        // given
        Member member = saveMember("user", List.of("레드", "화이트"), List.of("프랑스", "이탈리아"), 60000L);
        Wine wine1 = saveWine("와인1", "wine1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = saveWine("와인2", "wine2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = saveWine("와인3", "wine3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = saveWine("와인4", "wine4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = saveWine("와인5", "wine5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = saveWine("와인6", "wine6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = saveWine("와인7", "wine7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = saveWine("와인8", "wine8", "화이트", "독일", 250000, "리슬링", 4.3f);
        Wine wine9 = saveWine("와인9", "wine9", "스파클링", "프랑스", 35000, "샴페인", 4.6f);
        Wine wine10 = saveWine("와인10", "wine10", "디저트", "포르투갈", 45000, "포트 와인", 4.7f);
        Wine wine11 = saveWine("와인11", "wine11", "레드", "스페인", 55000, "템프라니요", 4.1f);
        Wine wine12 = saveWine("와인12", "wine12", "화이트", "뉴질랜드", 65000, "소비뇽 블랑", 4.4f);
        Wine wine13 = saveWine("와인13", "wine13", "레드", "칠레", 70000, "메를로", 4.3f);
        Wine wine14 = saveWine("와인14", "wine14", "화이트", "호주", 80000, "샤르도네", 4.1f);
        Wine wine15 = saveWine("와인15", "wine15", "로제", "프랑스", 90000, "그르나슈", 4.2f);
        Wine wine16 = saveWine("와인16", "wine16", "레드", "이탈리아", 100000, "산지오베제", 4.4f);
        Wine wine17 = saveWine("와인17", "wine17", "화이트", "스페인", 110000, "알바리뇨", 4.0f);
        Wine wine18 = saveWine("와인18", "wine18", "로제", "미국", 120000, "진판델", 4.5f);
        Wine wine19 = saveWine("와인19", "wine19", "레드", "아르헨티나", 130000, "말벡", 4.6f);
        Wine wine20 = saveWine("와인20", "wine20", "화이트", "뉴질랜드", 140000, "피노 그리", 4.3f);

        for(
                Wine wine : List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8, wine9, wine10,
                        wine11, wine12, wine13, wine14, wine15, wine16, wine17, wine18, wine19, wine20)) {
            WineVintage wineVintage = saveWineVintage(wine, 2017);

            for (int i = 0;
                 i < Integer.parseInt(wine.getName().replaceAll("\\D+", ""));
                 i++) {
                saveWineWishlist(wineVintage, member);
            }
        }

        // when
        List<HomeWineResponse> mostLikedWineList = wineService.getMostLikedWineList();
        // then
        assertThat(mostLikedWineList).hasSize(10)
                .extracting("wineName")
                .containsExactly("와인20", "와인19", "와인18", "와인17", "와인16", "와인15", "와인14", "와인13", "와인12", "와인11");

    }

    private WineWishlist saveWineWishlist(WineVintage wineVintage, Member member) {
        return wineWishlistRepository.save(WineWishlist.builder()
                .member(member)
                .wineVintage(wineVintage)
                .build());
    }

    private Wine saveWine(String name, String nameEng, String sort, String country, int price, String variety, float vivinoRating) {
        return wineRepository.save(Wine.builder()
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
                .price(price).build());
    }


    private TastingNote saveTastingNote(Member member, WineVintage wineVintage, String color,
                                        int sweetness, int acidity, int tannin, int body, int alcohol,
                                        float rating, String review) {
        return tastingNoteRepository.save(TastingNote.builder()
                .member(member)
                .wineVintage(wineVintage)
                .color(color)
                .tasteDate(LocalDate.of(2025, 1, 6))
                .sweetness(sweetness)
                .acidity(acidity)
                .tannin(tannin)
                .body(body)
                .alcohol(alcohol)
                .rating(rating)
                .review(review).build());
    }

    private Member saveMember(String username, List<String> wineSort, List<String> wineArea, Long monthPriceMax) {
        return memberRepository.save(Member.builder()
                .username(username)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .wineSort(wineSort)
                .wineArea(wineArea)
                .monthPriceMax(monthPriceMax)
                .build());
    }

    private WineVintage saveWineVintage(Wine wine, int vintageYear) {
        return wineVintageRepository.save(
                WineVintage.builder()
                        .wine(wine)
                        .vintageYear(vintageYear)
                        .build());
    }

    private void assertWinePreviewPageResponse(PageResponse<WinePreviewResponse> winePreviewResponsePageResponse, int pageNumber, int totalPages, List<WinePreviewResponse> winePreviewContents) {
        assertThat(winePreviewResponsePageResponse.getContent())
                .hasSize(winePreviewContents.size())
                .extracting("wineId", "name", "imageUrl", "sort", "country", "variety", "vivinoRating", "price")
                .containsExactly(
                        winePreviewContents.stream()
                                .map(content -> tuple(content.getWineId(), content.getName(), content.getImageUrl(),
                                        content.getSort(), content.getCountry(), content.getVariety(), content.getVivinoRating(), content.getPrice()))
                                .toArray(Tuple[]::new)
                );
        assertThat(winePreviewResponsePageResponse)
                .extracting("pageNumber", "totalPages")
                .containsExactly(pageNumber, totalPages);
    }

    private void assertWineReviewPageResponse(PageResponse<WineReviewResponse> wineReviewResponsePageResponse, int pageNumber, int totalPages, List<WineReviewResponse> reviewContents) {
        assertThat(wineReviewResponsePageResponse.getContent())
                .hasSize(reviewContents.size())
                .extracting("name", "review", "rating", "createdAt")
                .containsExactly(
                        reviewContents.stream()
                                .map(content -> tuple(content.getName(), content.getReview(), content.getRating(), content.getCreatedAt()))
                                .toArray(Tuple[]::new)
                );

        assertThat(wineReviewResponsePageResponse)
                .extracting("pageNumber", "totalPages")
                .containsExactly(pageNumber, totalPages);
    }


}