package com.drinkeg.drinkeg.domain.wine.repository;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.drinkeg.drinkeg.domain.member.domain.Member.createMember;
import static org.assertj.core.api.Assertions.assertThat;

class WineRepositoryImplTest extends IntegrationTestSupport {
    @Autowired
    WineRepository wineRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineWishlistRepository wineWishlistRepository;

    @DisplayName("멤버 선호 와인 종류, 지역, 가격대에 따른 와인 추천 리스트 조회")
    @Test
    void findRecommendWinesByMemberPreferSortAndAreaAndPrice() {
        // given
        Wine wine1 = createWine("와인1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "레드", "미국", 15000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "화이트", "독일", 25000, "리슬링", 4.3f);
        Wine wine9 = createWine("와인9", "스파클링", "프랑스", 35000, "샴페인", 4.6f);
        Wine wine10 = createWine("와인10", "디저트", "포르투갈", 45000, "포트 와인", 4.7f);
        Wine wine11 = createWine("와인11", "레드", "스페인", 55000, "템프라니요", 4.1f);
        Wine wine12 = createWine("와인12", "화이트", "뉴질랜드", 65000, "소비뇽 블랑", 4.4f);
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8, wine9, wine10, wine11, wine12));

        // when
         List<Wine> recommendWines = wineRepository.findRecommendWinesBy(List.of("프랑스", "이탈리아"), List.of("레드", "화이트"), 60000L);

         // then
         assertThat(recommendWines)
                 .hasSize(4)
                 .extracting("name")
                 .containsExactlyInAnyOrder("와인1", "와인2", "와인4", "와인5");
    }

    @DisplayName("멤버 선호 와인 종류, 가격대에 따른 와인 추천 리스트 조회")
    @Test
    void findRecommendWinesByMemberPreferSortAndPrice() {
        // given
        Wine wine1 = createWine("와인1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "레드", "미국", 15000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "화이트", "독일", 25000, "리슬링", 4.3f);
        Wine wine9 = createWine("와인9", "스파클링", "프랑스", 35000, "샴페인", 4.6f);
        Wine wine10 = createWine("와인10", "디저트", "포르투갈", 45000, "포트 와인", 4.7f);
        Wine wine11 = createWine("와인11", "레드", "스페인", 55000, "템프라니요", 4.1f);
        Wine wine12 = createWine("와인12", "화이트", "뉴질랜드", 65000, "소비뇽 블랑", 4.4f);
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8, wine9, wine10, wine11, wine12));

        // when
        List<Wine> recommendWines = wineRepository.findRecommendWinesBy(List.of(), List.of("레드", "화이트"), 60000L);

        // then
        assertThat(recommendWines)
                .hasSize(7)
                .extracting("name")
                .containsExactlyInAnyOrder("와인1", "와인2", "와인4", "와인5", "와인7", "와인8", "와인11");
    }

    @DisplayName("멤버 선호 가격대에 따른 와인 추천 리스트 조회")
    @Test
    void findRecommendWinesByMemberPreferPrice() {
        // given
        Wine wine1 = createWine("와인1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "레드", "미국", 15000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "화이트", "독일", 25000, "리슬링", 4.3f);
        Wine wine9 = createWine("와인9", "스파클링", "프랑스", 35000, "샴페인", 4.6f);
        Wine wine10 = createWine("와인10", "디저트", "포르투갈", 45000, "포트 와인", 4.7f);
        Wine wine11 = createWine("와인11", "레드", "스페인", 55000, "템프라니요", 4.1f);
        Wine wine12 = createWine("와인12", "화이트", "뉴질랜드", 65000, "소비뇽 블랑", 4.4f);
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8, wine9, wine10, wine11, wine12));

        // when
        List<Wine> recommendWines = wineRepository.findRecommendWinesBy(List.of(), List.of(), 60000L);

        // then
        assertThat(recommendWines)
                .hasSize(9)
                .extracting("name")
                .containsExactlyInAnyOrder("와인1", "와인2", "와인4", "와인5", "와인7", "와인8", "와인9", "와인10", "와인11");
    }

    @DisplayName("멤버의 선호 정보 없이 와인 추천 리스트 조회")
    @Test
    void findRecommendWinesByMember() {
        // given
        Wine wine1 = createWine("와인1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "화이트", "독일", 250000, "리슬링", 4.3f);
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8));

        // when
        List<Wine> recommendWines = wineRepository.findRecommendWinesBy(List.of(), List.of(), null);

        // then
        assertThat(recommendWines)
                .extracting("name")
                .containsExactlyInAnyOrder("와인1", "와인2", "와인4", "와인5");
    }

    @DisplayName("좋아요 수가 많은 와인 리스트 조회")
    @Test
    void findMostLikedWines() {
        // given
        Member member = memberRepository.save(creatMember("user"));
        Wine wine1 = wineRepository.save(createWine("와인1", "레드", "프랑스", 10000, "피노누아", 4.5f));
        Wine wine2 = wineRepository.save(createWine("와인2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f));
        Wine wine3 = wineRepository.save(createWine("와인3", "로제", "스페인", 30000, "피노누아", 3.5f));
        Wine wine4 = wineRepository.save(createWine("와인4", "레드", "프랑스", 40000, "피노누아", 4.5f));
        Wine wine5 = wineRepository.save(createWine("와인5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f));
        Wine wine6 = wineRepository.save(createWine("와인6", "로제", "스페인", 60000, "피노누아", 3.5f));
        Wine wine7 = wineRepository.save(createWine("와인7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f));
        Wine wine8 = wineRepository.save(createWine("와인8", "화이트", "독일", 250000, "리슬링", 4.3f));
        Wine wine9 = wineRepository.save(createWine("와인9", "스파클링", "프랑스", 35000, "샴페인", 4.6f));
        Wine wine10 = wineRepository.save(createWine("와인10", "디저트", "포르투갈", 45000, "포트 와인", 4.7f));
        Wine wine11 = wineRepository.save(createWine("와인11", "레드", "스페인", 55000, "템프라니요", 4.1f));
        Wine wine12 = wineRepository.save(createWine("와인12", "화이트", "뉴질랜드", 65000, "소비뇽 블랑", 4.4f));
        Wine wine13 = wineRepository.save(createWine("와인13", "레드", "칠레", 70000, "메를로", 4.3f));
        Wine wine14 = wineRepository.save(createWine("와인14", "화이트", "호주", 80000, "샤르도네", 4.1f));
        Wine wine15 = wineRepository.save(createWine("와인15", "로제", "프랑스", 90000, "그르나슈", 4.2f));
        Wine wine16 = wineRepository.save(createWine("와인16", "레드", "이탈리아", 100000, "산지오베제", 4.4f));
        Wine wine17 = wineRepository.save(createWine("와인17", "화이트", "스페인", 110000, "알바리뇨", 4.0f));
        Wine wine18 = wineRepository.save(createWine("와인18", "로제", "미국", 120000, "진판델", 4.5f));
        Wine wine19 = wineRepository.save(createWine("와인19", "레드", "아르헨티나", 130000, "말벡", 4.6f));
        Wine wine20 = wineRepository.save(createWine("와인20", "화이트", "뉴질랜드", 140000, "피노 그리", 4.3f));

        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8, wine9, wine10,
                                wine11, wine12, wine13, wine14, wine15, wine16, wine17, wine18, wine19, wine20));
        List<WineWishlist> wishlists = List.of(
                createWineWishlist(wine1, member), createWineWishlist(wine1, member), createWineWishlist(wine2, member),
                createWineWishlist(wine2, member), createWineWishlist(wine2, member), createWineWishlist(wine3, member),
                createWineWishlist(wine3, member), createWineWishlist(wine3, member), createWineWishlist(wine4, member),
                createWineWishlist(wine4, member), createWineWishlist(wine5, member), createWineWishlist(wine5, member),
                createWineWishlist(wine6, member), createWineWishlist(wine6, member), createWineWishlist(wine7, member),
                createWineWishlist(wine8, member), createWineWishlist(wine8, member), createWineWishlist(wine9, member),
                createWineWishlist(wine9, member), createWineWishlist(wine10, member), createWineWishlist(wine11, member),
                createWineWishlist(wine12, member), createWineWishlist(wine13, member), createWineWishlist(wine14, member),
                createWineWishlist(wine15, member), createWineWishlist(wine16, member), createWineWishlist(wine17, member),
                createWineWishlist(wine18, member), createWineWishlist(wine19, member), createWineWishlist(wine20, member));
        wineWishlistRepository.saveAll(wishlists);

        // when
        List<Wine> mostLikedWines = wineRepository.findMostLikedWines();

        // then
        assertThat(mostLikedWines)
                .hasSize(10)
                .extracting("name")
                .containsExactly("와인2", "와인3", "와인9", "와인1", "와인4", "와인8", "와인5", "와인6", "와인10", "와인19");
    }
  
    @DisplayName("와인 이름을 받아서 이름을 포함하는 모든 와인을 조회한다.")
    @Test
    void searchWineByName() {
        // given
        Wine wine1 = createWine("대중적인 레드 와인 10년");
        Wine wine2 = createWine("대중적인 화이트 와인 13년");
        Wine wine3 = createWine("대중적인 화이트 스파클링 와인 20년");
        Wine wine4 = createWine("매니아들이 찾는 레드 와인 30년");
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4));
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<Wine> wineList1 = wineRepository.searchByName("0년", pageable);
        List<Wine> wineList2 = wineRepository.searchByName("대중적", pageable);

        // then
        assertThat(wineList1).hasSize(3)
                .isEqualTo(List.of(wine1, wine3, wine4));
        assertThat(wineList2).hasSize(3)
                .isEqualTo(List.of(wine1, wine3, wine2));
    }

    @DisplayName("와인 이름을 영어로 받으면 영어로 된 와인 이름을 포함하는 모든 와인을 조회한다.")
    @Test
    void searchWineByNameEng() {
        // given
        Wine wine1 = createWine("대중적인 레드 와인 10년", "popular red wine 10 years");
        Wine wine2 = createWine("대중적인 화이트 와인 13년", "popular white wine 13 years");
        Wine wine3 = createWine("대중적인 화이트 스파클링 와인 20년", "popular white sparkling wine 20 years");
        Wine wine4 = createWine("매니아들이 찾는 레드 와인 30년", "red wine that manias find 30 years");
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4));
        Pageable pageable = PageRequest.of(0, 10);
        // when
        List<Wine> wineList1 = wineRepository.searchByName("0 years", pageable);
        List<Wine> wineList2 = wineRepository.searchByName("popular", pageable);

        // then
        assertThat(wineList1).hasSize(3)
                .isEqualTo(List.of(wine1, wine3, wine4));
        assertThat(wineList2).hasSize(3)
                .isEqualTo(List.of(wine1, wine3, wine2));
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
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<Wine> wineList = wineRepository.searchByName("존재하지 않는 와인 이름으로 검색하기", pageable);

        // then
        assertThat(wineList).isEmpty();
    }

    @DisplayName("와인 이름을 받아서 이름을 포함하는 모든 와인을 조회한다. (페이징)")
    @Test
    void searchWineByNameWithPaging() {
        // given
        Wine wine1 = createWine("대중적인 레드 와인 10년");
        Wine wine2 = createWine("대중적인 화이트 와인 13년");
        Wine wine3 = createWine("대중적인 화이트 스파클링 와인 20년");
        Wine wine4 = createWine("매니아들이 찾는 레드 와인 30년");
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4));
        Pageable pageable = Pageable.ofSize(2).withPage(1);

        // when
        List<Wine> wineList = wineRepository.searchByName("0년", pageable);

        // then
        assertThat(wineList).hasSize(1)
                .isEqualTo(List.of(wine4));
    }

    @DisplayName("검색한 와인 이름을 포함하는 와인의 총 개수를 조회한다.")
    @Test
    void countSearchWinePage(){
        // given
        Wine wine1 = createWine("대중적인 레드 와인 10년");
        Wine wine2 = createWine("대중적인 화이트 와인 13년");
        Wine wine3 = createWine("대중적인 화이트 스파클링 와인 20년");
        Wine wine4 = createWine("매니아들이 찾는 레드 와인 30년");
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4));

        // when
        long count = wineRepository.countSearchWinePage("0년");

        // then
        assertThat(count).isEqualTo(3);
    }

    @DisplayName("검색한 와인 이름을 포함하는 와인의 총 개수를 조회한다. (검색어 결과가 없는 경우)")
    @Test
    void countSearchWinePageWithEmptySearchName(){
        // given
        Wine wine1 = createWine("대중적인 레드 와인 10년");
        Wine wine2 = createWine("대중적인 화이트 와인 13년");
        Wine wine3 = createWine("대중적인 화이트 스파클링 와인 20년");
        Wine wine4 = createWine("매니아들이 찾는 레드 와인 30년");
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4));

        // when
        long count = wineRepository.countSearchWinePage("매력적인");

        // then
        assertThat(count).isEqualTo(0);
    }

    private Member creatMember(String username) {
        return Member.builder()
                .username(username)
                .build();
    }

    private WineWishlist createWineWishlist(Wine wine, Member member) {
        return WineWishlist.builder()
                .wine(wine)
                .member(member)
                .build();
    }

    private WineWishlist createWineWishlist(Wine wine) {
        return WineWishlist.builder()
                .member(memberRepository.save(createMember("user1", "password", true)))
                .wine(wine)
                .build();
    }

    private Wine createWine(String name) {
        return createWine(name, "default nameEng");
    }

    private Wine createWine(String name, String nameEng) {
        return Wine.builder()
                .name(name)
                .nameEng(nameEng)
                .imageUrl("http://default.image")
                .sort("레드")
                .country("프랑스")
                .variety("피노누아")
                .vivinoRating(4.5f)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(10000).build();
    }

    private Wine createWine(String name, String sort, String country, int price, String variety, float vivinoRating) {
        return Wine.builder()
                .name(name)
                .imageUrl("http://default.image")
                .sort(sort)
                .country(country)
                .variety(variety)
                .vivinoRating(vivinoRating)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(price).build();
    }
}