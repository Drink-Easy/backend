package com.drinkeg.drinkeg.domain.wineWishlist.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wineVintage.repository.WineVintageRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class WineWishlistServiceImplTest extends IntegrationTestSupport {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineRepository wineRepository;
    @Autowired
    WineWishlistRepository wineWishlistRepository;
    @Autowired
    WineVintageRepository wineVintageRepository;

    @Autowired
    WineWishlistService wineWishlistService;

    @DisplayName("빈티지를 선택하고 와인 위시리스트를 저장한다.")
    @Test
    void createWineWishlist() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));
        saveWineVintage(wine, 2017);

        //when
        Long wineWishlistId = wineWishlistService.createWineWishlist(wine.getId(), 2017, member.getUsername());

        //then
        Optional<WineWishlist> wineWishlist = wineWishlistRepository.findById(wineWishlistId);
        Assertions.assertThat(wineWishlist).isPresent();
        wineWishlist.ifPresent(note -> {
            assertThat(note.getMember().getId()).isEqualTo(member.getId());
            assertThat(note.getWineVintage().getWine().getId()).isEqualTo(wine.getId());
            assertThat(note.getWineVintage().getVintageYear()).isEqualTo(2017);
        });
    }

    @DisplayName("빈티지를 선택하지 않고 와인 위시리스트를 저장하면 빈티지 연도가 0으로 저장된다.")
    @Test
    void createWineWishlistWithoutVintage() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));
        saveWineVintage(wine, 0);

        //when
        Long wineWishlistId = wineWishlistService.createWineWishlist(wine.getId(), null, member.getUsername());

        //then
        Optional<WineWishlist> wineWishlist = wineWishlistRepository.findById(wineWishlistId);
        Assertions.assertThat(wineWishlist).isPresent();
        wineWishlist.ifPresent(note -> {
            assertThat(note.getMember().getId()).isEqualTo(member.getId());
            assertThat(note.getWineVintage().getWine().getId()).isEqualTo(wine.getId());
            assertThat(note.getWineVintage().getVintageYear()).isEqualTo(0);
        });
    }

    @DisplayName("존재하지 않는 회원으로 와인 위시리스트를 저장하면 MEMBER_NOT_FOUND 예외가 발생한다.")
    @Test
    void createWineWishlistByWrongMember() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        memberRepository.delete(member);
        Wine wine = wineRepository.save(createWine("wine1"));
        saveWineVintage(wine, 2017);

        //when && then
        assertThatThrownBy(() -> wineWishlistService.createWineWishlist(wine.getId(), 2017,"user1"))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("존재하지 않는 와인으로 와인 위시리스트를 저장하면 WINE_NOT_FOUND 예외가 발생한다.")
    @Test
    void createWineWishlistByWrongWine() {
        //given
        Member member = memberRepository.save(createMember("user1"));

        //when && then
        assertThatThrownBy(() -> wineWishlistService.createWineWishlist(-1L, 2017, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.WINE_NOT_FOUND.getMessage());
    }

    @DisplayName("이미 있는 와인 위시리스트를 다시 저장하면 WINE_WISHLIST_ALREADY_EXISTS 예외가 발생한다.")
    @Test
    void createWineWishlistAlreadyExists() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));
        WineVintage wineVintage = saveWineVintage(wine, 2017);
        wineWishlistRepository.save(WineWishlist.builder()
                .member(member)
                .wineVintage(wineVintage)
                .build());

        //when && then
        assertThatThrownBy(() -> wineWishlistService.createWineWishlist(wine.getId(), 2017, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.WINE_WISHLIST_ALREADY_EXISTS.getMessage());
    }

    @DisplayName("회원의 모든 와인 위시리스트를 조회한다.")
    @Test
    void getAllWineWishlistByMember() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        Wine wine1 = wineRepository.save(createWineDetail("와인1","wine1", "http://default.image1", "레드", "프랑스", "보르도", "샤도네이", 4.1f, 100));
        Wine wine2 = wineRepository.save(createWineDetail("와인2","wine2", "http://default.image2", "화이트", "이탈리아", "로마", "모스카토", 4.5f, 30));
        Wine wine3 = wineRepository.save(createWineDetail("와인3","wine3", "http://default.image3", "로제", "스페인", "바르셀로나", "페타이", 4.3f, 50));
        WineVintage wineVintage1 = saveWineVintage(wine1, 2017);
        WineVintage wineVintage2 = saveWineVintage(wine2, 2018);
        WineVintage wineVintage3 = saveWineVintage(wine3, 2019);

        wineWishlistRepository.save(WineWishlist.create(member, wineVintage1));
        wineWishlistRepository.save(WineWishlist.create(member, wineVintage2));
        wineWishlistRepository.save(WineWishlist.create(member, wineVintage3));

        //when
        List<WinePreviewResponse> allWineWishlistByMember = wineWishlistService.getAllWineWishlistByMember(member.getUsername());

        //then
        assertThat(allWineWishlistByMember).hasSize(3)
                .extracting("wineId", "name", "vintageYear","imageUrl",
                        "sort", "country", "region", "variety", "vivinoRating", "price")
                .containsExactly(
                        tuple(wine3.getId(), "와인3", 2019, "http://default.image3", "로제", "스페인", "바르셀로나", "페타이", 4.3f, 50),
                        tuple(wine2.getId(), "와인2", 2018, "http://default.image2", "화이트", "이탈리아", "로마", "모스카토", 4.5f, 30),
                        tuple(wine1.getId(), "와인1" ,2017, "http://default.image1", "레드", "프랑스", "보르도", "샤도네이", 4.1f, 100)

                );
    }

    @DisplayName("회원의 와인 위시리스트가 비어있으면 빈 리스트를 반환한다.")
    @Test
    void getAllWineWishlistByMemberEmptyWishlist() {
        //given
        Member member = memberRepository.save(createMember("user1"));

        //when
        List<WinePreviewResponse> allWineWishlistByMember = wineWishlistService.getAllWineWishlistByMember(member.getUsername());

        //then
        assertThat(allWineWishlistByMember).isEmpty();
    }

    @DisplayName("존재하지 않는 회원으로 와인 위시리스트를 조회하면 MEMBER_NOT_FOUND 예외가 발생한다.")
    @Test
    void getAllWineWishlistByMemberByWrongMember() {
        //given
        Member wrongMember = memberRepository.save(createMember("wrongUser"));
        memberRepository.delete(wrongMember);

        Member member = memberRepository.save(createMember("user1"));
        Wine wine1 = wineRepository.save(createWineDetail("와인1","wine1", "http://default.image1", "레드", "프랑스", "보르도", "샤도네이", 4.1f, 100));
        saveWineVintage(wine1, 2017);

                //when & then
        assertThatThrownBy(() -> wineWishlistService.getAllWineWishlistByMember("wrongUser"))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("회원의 와인 위시리스트를 삭제한다.")
    @Test
    void deleteWineWishlist() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));
        WineVintage wineVintage1 = saveWineVintage(wine, 2017);
        WineWishlist wishlist = WineWishlist.create(member, wineVintage1);
        wineWishlistRepository.save(wishlist);

        //when
        wineWishlistService.deleteWineWishlist(wine.getId(), 2017, member.getUsername());

        //then
        assertThat(wineWishlistRepository.existsById(wishlist.getId())).isFalse();
    }

    @DisplayName("존재하지 않는 회원으로 와인 위시리스트를 삭제하면 MEMBER_NOT_FOUND 예외가 발생한다.")
    @Test
    void deleteWineWishlistByWrongMember() {
        //given
        Member wrongMember = memberRepository.save(createMember("wrongUser"));
        memberRepository.delete(wrongMember);

        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));
        WineVintage wineVintage1 = saveWineVintage(wine, 2017);
        wineWishlistRepository.save(WineWishlist.create(member, wineVintage1));

        //when & then
        assertThatThrownBy(() -> wineWishlistService.deleteWineWishlist(wine.getId(), 2017,"wrongUser"))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("존재하지 않는 와인으로 와인 위시리스트를 삭제하면 WINE_NOT_FOUND 예외가 발생한다.")
    @Test
    void deleteWineWishlistByWrongWine() {
        //given
        Member member = memberRepository.save(createMember("user1"));

        //when & then
        assertThatThrownBy(() -> wineWishlistService.deleteWineWishlist(-1L, 2017, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.WINE_NOT_FOUND.getMessage());
    }

    @DisplayName("회원의 와인 위시리스트가 없는데 삭제하면 WINE_WISHLIST_NOT_FOUND 예외가 발생한다.")
    @Test
    void deleteWrongWineWishlist() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));
        saveWineVintage(wine, 2017);

        //when & then
        assertThatThrownBy(() -> wineWishlistService.deleteWineWishlist(wine.getId(),2017, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.WINE_WISHLIST_NOT_FOUND.getMessage());
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

    private Wine createWineDetail(String name, String nameEng, String imageUrl, String sort, String country, String region, String variety, float vivinoRating, int price) {
        return Wine.builder()
                .name(name)
                .nameEng(nameEng)
                .imageUrl(imageUrl)
                .sort(sort)
                .country(country)
                .region(region)
                .variety(variety)
                .vivinoRating(vivinoRating)
                .price(price)
                .build();
    }

    private Member createMember(String username) {
        return Member.builder()
                .username(username)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .build();
    }

    private WineVintage saveWineVintage(Wine wine, int vintageYear) {
        return wineVintageRepository.save(WineVintage.builder()
                .wine(wine)
                .vintageYear(vintageYear)
                .wineNoteStatistics(WineNoteStatistics.create())
                .build());
    }
}