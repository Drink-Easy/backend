package com.drinkeg.drinkeg.domain.wine.repository;

import com.drinkeg.drinkeg.DatabaseCleaner;
import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wineVintage.repository.WineVintageRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;

class WineRepositoryImplTest extends IntegrationTestSupport {
    @Autowired
    WineRepository wineRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineWishlistRepository wineWishlistRepository;
    @Autowired
    WineVintageRepository wineVintageRepository;
    @Autowired
    DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

    @DisplayName("멤버 선호 와인 종류, 지역, 가격대에 따른 와인 추천 리스트 조회")
    @Test
    void findRecommendWinesByMemberPreferSortAndAreaAndPrice() {
        // given
        saveExampleWines();

        // when
         List<Wine> recommendWines = wineRepository.findRecommendWinesBy(List.of("프랑스", "이탈리아"), List.of("레드", "화이트"), 60000L);

         // then
         assertThat(recommendWines)
                 .hasSize(2)
                 .extracting("name")
                 .containsExactlyInAnyOrder("와인1", "와인4");
    }

    @DisplayName("멤버 선호 와인 종류, 가격대에 따른 와인 추천 리스트 조회")
    @Test
    void findRecommendWinesByMemberPreferSortAndPrice() {
        // given
        saveExampleWines();

        // when
        List<Wine> recommendWines = wineRepository.findRecommendWinesBy(List.of(), List.of("레드", "화이트"), 60000L);

        // then
        assertThat(recommendWines)
                .hasSize(3)
                .extracting("name")
                .containsExactlyInAnyOrder("와인1", "와인4", "와인8");
    }

    @DisplayName("멤버 선호 가격대에 따른 와인 추천 리스트 조회")
    @Test
    void findRecommendWinesByMemberPreferPrice() {
        // given
        saveExampleWines();

        // when
        List<Wine> recommendWines = wineRepository.findRecommendWinesBy(List.of(), List.of(), 60000L);

        // then
        assertThat(recommendWines)
                .hasSize(5)
                .extracting("name")
                .containsExactlyInAnyOrder("와인1", "와인4", "와인8", "와인9", "와인10");
    }

    @DisplayName("멤버의 선호 정보 없이 와인 추천 리스트 조회")
    @Test
    void findRecommendWinesByMember() {
        // given
        saveExampleWines();

        // when
        List<Wine> recommendWines = wineRepository.findRecommendWinesBy(List.of(), List.of(), null);

        // then
        assertThat(recommendWines)
                .hasSize(5)
                .extracting("name")
                .containsExactlyInAnyOrder("와인1", "와인4", "와인8", "와인9", "와인10");
    }

    @DisplayName("좋아요 수가 많은 와인 리스트 조회")
    @Test
    void findMostLikedWines() {
        // given
        Member member = saveMember("testUser");
        Map<Wine, WineVintage> wineWineVintageMap = saveExampleWines();
        List<WineVintage> wineVintages = new ArrayList<>(wineWineVintageMap.values());

        wineWishlistRepository.saveAll(
                List.of(
                        createWineWishlist(wineVintages.get(0), member),
                        createWineWishlist(wineVintages.get(0), member),
                        createWineWishlist(wineVintages.get(0), member),
                        createWineWishlist(wineVintages.get(0), member),
                        createWineWishlist(wineVintages.get(0), member),

                        createWineWishlist(wineVintages.get(2), member),
                        createWineWishlist(wineVintages.get(2), member),
                        createWineWishlist(wineVintages.get(2), member),

                        createWineWishlist(wineVintages.get(3), member),
                        createWineWishlist(wineVintages.get(3), member),
                        createWineWishlist(wineVintages.get(4), member),
                        createWineWishlist(wineVintages.get(5), member),
                        createWineWishlist(wineVintages.get(6), member),
                        createWineWishlist(wineVintages.get(7), member),
                        createWineWishlist(wineVintages.get(8), member)
                )
        );

        // when
        List<Wine> mostLikedWines = wineRepository.findMostLikedWines();

        // then
        assertThat(mostLikedWines).hasSize(10);

        List<String> actualOrder = mostLikedWines.stream()
                .map(Wine::getName)
                .collect(toList());

         assertThat(actualOrder).containsExactly(
                "와인1",
                "와인3",
                "와인4",
                "와인9",
                "와인8",
                "와인7",
                "와인5",
                "와인6",
                "와인10",
                "와인12"
        );
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
        Map<Wine, WineVintage> wineWineVintageMap = saveExampleWines();
        Set<Wine> wines = wineWineVintageMap.keySet();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        List<Wine> wineList1 = wineRepository.searchByName("wine1", pageable);

        // then
        assertThat(wineList1).hasSize(4)
                .isEqualTo(List.of(
                       wines.stream().filter(wine -> wine.getNameEng().equals("wine1")).findFirst().orElseThrow(),
                          wines.stream().filter(wine -> wine.getNameEng().equals("wine10")).findFirst().orElseThrow(),
                            wines.stream().filter(wine -> wine.getNameEng().equals("wine11")).findFirst().orElseThrow(),
                            wines.stream().filter(wine -> wine.getNameEng().equals("wine12")).findFirst().orElseThrow()
                ));
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
    void countSearchWine(){
        // given
        Wine wine1 = createWine("대중적인 레드 와인 10년");
        Wine wine2 = createWine("대중적인 화이트 와인 13년");
        Wine wine3 = createWine("대중적인 화이트 스파클링 와인 20년");
        Wine wine4 = createWine("매니아들이 찾는 레드 와인 30년");
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4));

        // when
        long count = wineRepository.countSearchWine("0년");

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
        long count = wineRepository.countSearchWine("매력적인");

        // then
        assertThat(count).isEqualTo(0);
    }

    @DisplayName("와인 이름, 종류, 품종, 국가 정보를 받아 해당하는 와인을 조회한다.")
    @Test
    void searchWineAdmin(){
        //given
        saveExampleWines();
        Pageable pageable = PageRequest.of(0, 7);

        //when
        List<Wine> wineList1 = wineRepository.searchByNameSortVarietyAndArea("와인","레드", "피노누아", "프랑스", pageable);

        //then
        assertThat(wineList1).hasSize(2)
                .extracting(Wine::getName)
                .containsExactlyInAnyOrder("와인1", "와인4");
    }

    @DisplayName("와인 종류로 와인을 조회한다.")
    @Test
    void searchWineAdminBySort(){
        //given
        saveExampleWines();
        Pageable pageable = PageRequest.of(0, 7);

        //when
        List<Wine> wineList1 = wineRepository.searchByNameSortVarietyAndArea(null,"레드", null, null, pageable);

        //then
        assertThat(wineList1).hasSize(4)
                .extracting(Wine::getName)
                .containsExactlyInAnyOrder("와인1", "와인11", "와인4", "와인7");
    }

    @DisplayName("와인 품종으로 와인을 조회한다.")
    @Test
    void searchWineAdminByVariety(){
        //given
        saveExampleWines();
        Pageable pageable = PageRequest.of(0, 7);

        //when
        List<Wine> wineList1 = wineRepository.searchByNameSortVarietyAndArea(null,null, "피노누아", null, pageable);

        //then
        assertThat(wineList1).hasSize(4)
                .extracting(Wine::getName)
                .containsExactlyInAnyOrder("와인1", "와인3", "와인4", "와인6");
    }

    @DisplayName("와인 국가로 와인을 조회한다.")
    @Test
    void searchWineAdminByArea(){
        //given
        saveExampleWines();
        Pageable pageable = PageRequest.of(0, 7);

        //when
        List<Wine> wineList1 = wineRepository.searchByNameSortVarietyAndArea(null,null, null, "프랑스", pageable);

        //then
        assertThat(wineList1).hasSize(3)
                .extracting(Wine::getName)
                .containsExactlyInAnyOrder("와인1", "와인4", "와인9");
    }

    @DisplayName("와인 이름, 종류, 품종, 국가 정보를 받아 해당하는 와인의 총 개수를 조회한다.")
    @Test
    void countSearchWineBySortVarietyAndArea(){
        //given
        saveExampleWines();

        //when
        long count = wineRepository.countSearchWineSortVarietyAndArea("1","레드", "피노누아", "프랑스");

        //then
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("와인 이름으로 와인의 총 개수를 조회한다.")
    @Test
    void countSearchWineByName(){
        //given
        saveExampleWines();

        //when
        long count = wineRepository.countSearchWineSortVarietyAndArea("1",null, null, null);

        //then
        assertThat(count).isEqualTo(4);
    }

    @DisplayName("와인 종류로 와인의 총 개수를 조회한다.")
    @Test
    void countSearchWineBySort(){
        //given
        saveExampleWines();

        //when
        long count = wineRepository.countSearchWineSortVarietyAndArea(null,"레드", null, "");

        //then
        assertThat(count).isEqualTo(4);
    }

    @DisplayName("와인 품종으로 와인의 총 개수를 조회한다.")
    @Test
    void countSearchWineByVariety(){
        //given
        saveExampleWines();

        //when
        long count = wineRepository.countSearchWineSortVarietyAndArea(null,null, "피노누아", "");

        //then
        assertThat(count).isEqualTo(4);
    }

    @DisplayName("와인 국가로 와인의 총 개수를 조회한다.")
    @Test
    void countSearchWineByArea(){
        //given
        saveExampleWines();

        //when
        long count = wineRepository.countSearchWineSortVarietyAndArea(null,null, null, "프랑스");

        //then
        assertThat(count).isEqualTo(3);
    }

    private Member saveMember(String username) {
        return memberRepository.save(Member.builder()
                .username(username)
                .build());
    }

    private WineWishlist createWineWishlist(WineVintage wineVintage, Member member) {
        return WineWishlist.builder()
                .wineVintage(wineVintage)
                .member(member)
                .build();
    }


    private Wine createWine(String name) {
        return createWine(name, "default nameEng");
    }

    private Wine createWine(String name, String nameEng) {
        String cleanName = name.replaceAll("[ ,.'\\\\]", "").toLowerCase();
        String cleanNameEng = nameEng.replaceAll("[ ,.'\\\\]", "").toLowerCase();
        return Wine.builder()
                .name(name)
                .nameEng(nameEng)
                .imageUrl("http://default.image")
                .sort("레드")
                .country("프랑스")
                .variety("피노누아")
                .vivinoRating(4.5f)
                .searchName(cleanName.concat(cleanNameEng))
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(10000).build();
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

    private Map<Wine, WineVintage> saveExampleWines() {
        Wine wine1 = createWine("와인1", "wine1", "레드", "프랑스", 10000, "피노누아", 4.5f);
        Wine wine2 = createWine("와인2","wine2", "화이트", "이탈리아", 20000, "샤르도네", 4.0f);
        Wine wine3 = createWine("와인3","wine3", "로제", "스페인", 30000, "피노누아", 3.5f);
        Wine wine4 = createWine("와인4","wine4", "레드", "프랑스", 40000, "피노누아", 4.5f);
        Wine wine5 = createWine("와인5","wine5", "화이트", "이탈리아", 50000, "샤르도네", 4.0f);
        Wine wine6 = createWine("와인6","wine6", "로제", "스페인", 60000, "피노누아", 3.5f);
        Wine wine7 = createWine("와인7","wine7", "레드", "미국", 15000, "카베르네 소비뇽", 4.2f);
        Wine wine8 = createWine("와인8","wine8", "화이트", "독일", 25000, "리슬링", 4.3f);
        Wine wine9 = createWine("와인9","wine9", "스파클링", "프랑스", 35000, "샴페인", 4.6f);
        Wine wine10 = createWine("와인10","wine10", "디저트", "포르투갈", 45000, "포트 와인", 4.7f);
        Wine wine11 = createWine("와인11","wine11", "레드", "스페인", 55000, "템프라니요", 4.1f);
        Wine wine12 = createWine("와인12","wine12", "화이트", "뉴질랜드", 65000, "소비뇽 블랑", 4.4f);
        wineRepository.saveAll(List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8, wine9, wine10, wine11, wine12));

        Map<Wine, WineVintage> wineAndVintages = new LinkedHashMap<>();

        for(Wine wine : List.of(wine1, wine2, wine3, wine4, wine5, wine6, wine7, wine8, wine9, wine10, wine11, wine12)) {
            WineVintage wineVintage = createWineVintage(wine, 2017);
            wineVintageRepository.save(wineVintage);
            wineAndVintages.put(wine, wineVintage);
        }

        return wineAndVintages;
    }
}