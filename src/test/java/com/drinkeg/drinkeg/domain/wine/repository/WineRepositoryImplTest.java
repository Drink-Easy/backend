package com.drinkeg.drinkeg.domain.wine.repository;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
//                .hasSize(8)
                .extracting("name")
                .containsExactlyInAnyOrder("와인1", "와인2", "와인4", "와인5");
    }

    @DisplayName("좋아요 수가 많은 와인 리스트 조회")
    @Test
    void findMostLikedWines() {
        // given
        Wine wine1 = createWine("와인1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7", "레드", "미국", 150000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8", "화이트", "독일", 250000, "리슬링", 4.3f);
        Wine wine9 = createWine("와인9", "스파클링", "프랑스", 35000, "샴페인", 4.6f);
        Wine wine10 = createWine("와인10", "디저트", "포르투갈", 45000, "포트 와인", 4.7f);
        Wine wine11 = createWine("와인11", "레드", "스페인", 55000, "템프라니요", 4.1f);
        Wine wine12 = createWine("와인12", "화이트", "뉴질랜드", 65000, "소비뇽 블랑", 4.4f);
        Wine wine13 = createWine("와인13", "레드", "칠레", 70000, "메를로", 4.3f);
        Wine wine14 = createWine("와인14", "화이트", "호주", 80000, "샤르도네", 4.1f);
        Wine wine15 = createWine("와인15", "로제", "프랑스", 90000, "그르나슈", 4.2f);
        Wine wine16 = createWine("와인16", "레드", "이탈리아", 100000, "산지오베제", 4.4f);
        Wine wine17 = createWine("와인17", "화이트", "스페인", 110000, "알바리뇨", 4.0f);
        Wine wine18 = createWine("와인18", "로제", "미국", 120000, "진판델", 4.5f);
        Wine wine19 = createWine("와인19", "레드", "아르헨티나", 130000, "말벡", 4.6f);
        Wine wine20 = createWine("와인20", "화이트", "뉴질랜드", 140000, "피노 그리", 4.3f);

        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8, wine9, wine10,
                                wine11, wine12, wine13, wine14, wine15, wine16, wine17, wine18, wine19, wine20));
        List<WineWishlist> wishlists = List.of(
                createWineWishlist(wine1), createWineWishlist(wine1), createWineWishlist(wine2), createWineWishlist(wine2), createWineWishlist(wine2),
                createWineWishlist(wine3), createWineWishlist(wine3), createWineWishlist(wine3), createWineWishlist(wine4), createWineWishlist(wine4),
                createWineWishlist(wine5), createWineWishlist(wine5), createWineWishlist(wine6), createWineWishlist(wine6), createWineWishlist(wine7),
                createWineWishlist(wine8), createWineWishlist(wine8), createWineWishlist(wine9), createWineWishlist(wine9), createWineWishlist(wine10),
                createWineWishlist(wine11), createWineWishlist(wine12), createWineWishlist(wine13), createWineWishlist(wine14), createWineWishlist(wine15),
                createWineWishlist(wine16), createWineWishlist(wine17), createWineWishlist(wine18), createWineWishlist(wine19), createWineWishlist(wine20));
        wineWishlistRepository.saveAll(wishlists);

        // when
        List<Wine> mostLikedWines = wineRepository.findMostLikedWines();

        // then
        assertThat(mostLikedWines)
                .hasSize(10)
                .extracting("name")
                .containsExactlyInAnyOrder("와인2", "와인3", "와인9", "와인1", "와인4", "와인8", "와인5", "와인6", "와인10", "와인19");
    }

    private WineWishlist createWineWishlist(Wine wine) {
        return WineWishlist.builder()
                .wine(wine)
                .build();
    }

    private Wine createWine(String name, String sort, String area, int price, String variety, float vivinoRating) {
        return Wine.builder()
                .name(name)
                .imageUrl("http://default.image")
                .sort(sort)
                .area(area)
                .variety(variety)
                .vivinoRating(vivinoRating)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(price).build();
    }
}