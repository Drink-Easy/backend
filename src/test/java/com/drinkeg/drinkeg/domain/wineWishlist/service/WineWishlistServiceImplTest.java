package com.drinkeg.drinkeg.domain.wineWishlist.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
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
    WineWishlistService wineWishlistService;

    @DisplayName("와인 위시리스트를 저장한다.")
    @Test
    void createWineWishlist() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));

        //when
        Long wineWishlistId = wineWishlistService.createWineWishlist(wine.getId(), member.getUsername());

        //then
        Optional<WineWishlist> wineWishlist = wineWishlistRepository.findById(wineWishlistId);
        Assertions.assertThat(wineWishlist).isPresent();
        wineWishlist.ifPresent(note -> {
            assertThat(note.getMember().getId()).isEqualTo(member.getId());
            assertThat(note.getWine().getId()).isEqualTo(wine.getId());
        });
    }

    @DisplayName("존재하지 않는 회원으로 와인 위시리스트를 저장하면 MEMBER_NOT_FOUND 예외가 발생한다.")
    @Test
    void createWineWishlist_MemberNotFound() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        memberRepository.delete(member);
        Wine wine = wineRepository.save(createWine("wine1"));

        //when && then
        assertThatThrownBy(() -> wineWishlistService.createWineWishlist(wine.getId(), "user1"))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("존재하지 않는 와인으로 와인 위시리스트를 저장하면 WINE_NOT_FOUND 예외가 발생한다.")
    @Test
    void createWineWishlist_WineNotFound() {
        //given
        Member member = memberRepository.save(createMember("user1"));

        //when && then
        assertThatThrownBy(() -> wineWishlistService.createWineWishlist(-1L, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.WINE_NOT_FOUND.getMessage());
    }

    @DisplayName("이미 있는 와인 위시리스트를 다시 저장하면 WINE_WISHLIST_ALREADY_EXISTS 예외가 발생한다.")
    @Test
    void createWineWishlist_WishlistAlreadyExixts() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));
        wineWishlistRepository.save(WineWishlist.create(member, wine));

        //when && then
        assertThatThrownBy(() -> wineWishlistService.createWineWishlist(wine.getId(), member.getUsername()))
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
        wineWishlistRepository.save(WineWishlist.create(member, wine1));
        wineWishlistRepository.save(WineWishlist.create(member, wine2));
        wineWishlistRepository.save(WineWishlist.create(member, wine3));

        //when
        List<WinePreviewResponse> allWineWishlistByMember = wineWishlistService.getAllWineWishlistByMember(member.getUsername());

        //then
        assertThat(allWineWishlistByMember).hasSize(3)
                .extracting("wineId", "name", "nameEng", "imageUrl",
                        "sort", "country", "region", "variety", "vivinoRating", "price")
                .containsExactly(
                        tuple(wine3.getId(), "와인3" ,"wine3", "http://default.image3", "로제", "스페인", "바르셀로나", "페타이", 4.3f, 50),
                        tuple(wine2.getId(), "와인2" ,"wine2", "http://default.image2", "화이트", "이탈리아", "로마", "모스카토", 4.5f, 30),
                        tuple(wine1.getId(), "와인1" ,"wine1", "http://default.image1", "레드", "프랑스", "보르도", "샤도네이", 4.1f, 100)

                );
    }

    @DisplayName("회원의 와인 위시리스트가 비어있으면 빈 리스트를 반환한다.")
    @Test
    void getAllWineWishlistByMember_EmptyWishlist() {
        //given
        Member member = memberRepository.save(createMember("user1"));

        //when
        List<WinePreviewResponse> allWineWishlistByMember = wineWishlistService.getAllWineWishlistByMember(member.getUsername());

        //then
        assertThat(allWineWishlistByMember).isEmpty();
    }

    @DisplayName("존재하지 않는 회원으로 와인 위시리스트를 조회하면 MEMBER_NOT_FOUND 예외가 발생한다.")
    @Test
    void getAllWineWishlistByMember_MemberNotFound() {
        //given
        Member wrongMember = memberRepository.save(createMember("wrongUser"));
        memberRepository.delete(wrongMember);

        Member member = memberRepository.save(createMember("user1"));
        Wine wine1 = wineRepository.save(createWineDetail("와인1","wine1", "http://default.image1", "레드", "프랑스", "보르도", "샤도네이", 4.1f, 100));
        wineWishlistRepository.save(WineWishlist.create(member, wine1));

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
        WineWishlist wishlist = WineWishlist.create(member, wine);
        wineWishlistRepository.save(wishlist);

        //when
        wineWishlistService.deleteWineWishlist(wine.getId(), member.getUsername());

        //then
        assertThat(wineWishlistRepository.existsById(wishlist.getId())).isFalse();
    }

    @DisplayName("존재하지 않는 회원으로 와인 위시리스트를 삭제하면 MEMBER_NOT_FOUND 예외가 발생한다.")
    @Test
    void deleteWineWishlist_MemberNotFound() {
        //given
        Member wrongMember = memberRepository.save(createMember("wrongUser"));
        memberRepository.delete(wrongMember);

        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));
        wineWishlistRepository.save(WineWishlist.create(member, wine));

        //when & then
        assertThatThrownBy(() -> wineWishlistService.deleteWineWishlist(wine.getId(), "wrongUser"))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("존재하지 않는 와인으로 와인 위시리스트를 삭제하면 WINE_NOT_FOUND 예외가 발생한다.")
    @Test
    void deleteWineWishlist_WineNotFound() {
        //given
        Member member = memberRepository.save(createMember("user1"));

        //when & then
        assertThatThrownBy(() -> wineWishlistService.deleteWineWishlist(-1L, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.WINE_NOT_FOUND.getMessage());
    }

    @DisplayName("회원의 와인 위시리스트가 없는데 삭제하면 WINE_WISHLIST_NOT_FOUND 예외가 발생한다.")
    @Test
    void deleteWineWishlist_WishlistNotFound() {
        //given
        Member member = memberRepository.save(createMember("user1"));
        Wine wine = wineRepository.save(createWine("wine1"));

        //when & then
        assertThatThrownBy(() -> wineWishlistService.deleteWineWishlist(wine.getId(), member.getUsername()))
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
}