package com.drinkeg.drinkeg.domain.myWine.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineUpdateRequest;
import com.drinkeg.drinkeg.domain.myWine.domain.MyWine;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWineResponse;
import com.drinkeg.drinkeg.domain.myWine.repository.MyWineRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MyWineServiceImplTest extends IntegrationTestSupport {
    @Autowired
    MyWineService myWineService;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineRepository wineRepository;
    @Autowired
    MyWineRepository myWineRepository;

    @DisplayName("특정 멤버의 보유와인 추가한다.")
    @Test
    void saveMyWine() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        MyWineRequest myWineRequest = createMyWineRequest(wine.getId(), LocalDate.parse("2025-01-11"), 100000);

        // when
        Long myWineId = myWineService.saveMyWine(myWineRequest, member.getUsername());

        // then
        Optional<MyWine> myWine = myWineRepository.findById(myWineId);
        assertThat(myWine.get())
                .extracting("id", "member", "wine", "purchaseDate", "purchasePrice")
                .containsExactly(myWineId, member, wine, LocalDate.parse("2025-01-11"), 100000);
    }

    @DisplayName("없는 멤버가 보유와인 추가하려고 하면 MEMBER_NOT_FOUND 예외 발생")
    @Test
    void saveMyWineByWrongMember() {
        // given
        Member member = createMember("wrongMember");
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        MyWineRequest myWineRequest = createMyWineRequest(wine.getId(), LocalDate.parse("2025-01-11"), 100000);

        // when
        assertThatThrownBy(() -> myWineService.saveMyWine(myWineRequest, "wrongMember "))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("없는 와인을 보유와인 추가하려고 하면 WINE_NOT_FOUND 예외 발생")
    @Test
    void saveMyWineByWrongWine() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        MyWineRequest myWineRequest = createMyWineRequest(-1L, LocalDate.parse("2025-01-11"), 100000);

        // when
        assertThatThrownBy(() -> myWineService.saveMyWine(myWineRequest, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.WINE_NOT_FOUND.getMessage());
    }

    @DisplayName("특정 보유와인을 조회한다.")
    @Test
    void getMyWineById() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        Long myWineId = saveMyWine(member, wine, LocalDate.parse("2025-01-11"), 100000);

        // when
        MyWineResponse myWine = myWineService.getMyWineById(myWineId, member.getUsername(), LocalDate.of(2025, 1, 11));

        // then
        assertThat(myWine)
                .extracting("myWineId", "wineId", "wineName", "wineSort",
                        "wineCountry", "wineRegion", "wineVariety", "wineImageUrl", "purchaseDate", "purchasePrice", "period")
                .containsExactly(myWineId, wine.getId(), wine.getName(), wine.getSort(),
                        wine.getCountry(), wine.getRegion(), wine.getVariety(), wine.getImageUrl(), LocalDate.parse("2025-01-11"), 100000, 0);
    }

    @DisplayName("없는 보유와인을 조회하려고 하면 MY_WINE_NOT_FOUND 예외 발생")
    @Test
    void getMyWineByIdByWrongMyWine() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> myWineService.getMyWineById(-1L, member.getUsername(), LocalDate.of(2025, 1, 1)))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MY_WINE_NOT_FOUND.getMessage());
    }

    @DisplayName("없는 멤버가 보유와인을 조회하려고 하면 MEMBER_NOT_FOUND 예외 발생")
    @Test
    void getMyWineByIdByWrongMember() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        Long myWineId = saveMyWine(member, wine, LocalDate.parse("2025-01-11"), 100000);

        // when & then
        assertThatThrownBy(() -> myWineService.getMyWineById(myWineId, "wrongMember", LocalDate.of(2025, 1, 1)))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("다른 멤버의 보유와인을 조회하려고 하면 MY_WINE_UNAUTHORIZED 예외 발생")
    @Test
    void getUnauthorizedMyWineById() {
        // given
        Member member1 = createMember("user");
        memberRepository.save(member1);
        Member member2 = createMember("user2");
        memberRepository.save(member2);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        Long myWineId = saveMyWine(member1, wine, LocalDate.parse("2025-01-11"), 100000);

        // when & then
        assertThatThrownBy(() -> myWineService.getMyWineById(myWineId, member2.getUsername(), LocalDate.of(2025, 1, 1)))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MY_WINE_UNAUTHORIZED.getMessage());
    }


    @DisplayName("특정 멤버의 보유와인을 최근 추가한 순으로 조회한다.")
    @Test
    void getMyWinesByUsername() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine1 = createWine("와인1");
        wineRepository.save(wine1);
        Wine wine2 = createWine("와인2");
        wineRepository.save(wine2);
        saveMyWine(member, wine1, LocalDate.parse("2025-01-11"), 100000);
        saveMyWine(member, wine2, LocalDate.parse("2025-01-12"), 200000);

        // when
        List<MyWineResponse> myWines = myWineService.getMyWinesByUsername(member.getUsername(), LocalDate.of(2025, 1, 12));

        // then
        assertThat(myWines).hasSize(2)
                .extracting("wineId", "wineName", "wineSort", "wineCountry", "wineRegion", "wineVariety", "wineImageUrl", "purchaseDate", "purchasePrice", "period")
                .containsExactly(
                        Assertions.tuple(wine2.getId(), wine2.getName(), wine2.getSort(), wine2.getCountry(), wine2.getRegion(), wine2.getVariety(),wine2.getImageUrl(), LocalDate.parse("2025-01-12"), 200000, 0),
                        Assertions.tuple(wine1.getId(), wine1.getName(), wine1.getSort(), wine1.getCountry(), wine1.getRegion(), wine1.getVariety(),wine1.getImageUrl(), LocalDate.parse("2025-01-11"), 100000, 1)
                );
    }

    @DisplayName("특정 멤버의 보유와인이 없으면 빈 리스트를 반환한다.")
    @Test
    void getMyWinesByEmpty() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);

        // when
        List<MyWineResponse> myWinesByUsername = myWineService.getMyWinesByUsername(member.getUsername(), LocalDate.of(2025, 1, 11));

        // then
        assertThat(myWinesByUsername).isEmpty();
    }

    @DisplayName("없는 멤버의 보유와인을 조회하려고 하면 MEMBER_NOT_FOUND 예외 발생")
    @Test
    void getMyWinesByWrongMember() {
        // when & then
        assertThatThrownBy(() -> myWineService.getMyWinesByUsername("wrongMember", LocalDate.of(2025, 1, 11)))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("특정 보유와인을 수정한다.")
    @Test
    void updateMyWine() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        Long myWineId = saveMyWine(member, wine, LocalDate.parse("2024-12-11"), 100000);
        MyWineUpdateRequest myWineUpdateRequest = createMyWineUpdateRequest(LocalDate.parse("2025-01-11"), 200000);

        // when
        myWineService.updateMyWine(myWineId, myWineUpdateRequest, member.getUsername());

        // then
        Optional<MyWine> updatedMyWine = myWineRepository.findById(myWineId);
        assertThat(updatedMyWine.get())
                .extracting("id", "member", "wine", "purchaseDate", "purchasePrice")
                .containsExactly(myWineId, member, wine, LocalDate.parse("2025-01-11"), 200000);
    }

    @DisplayName("없는 보유와인을 수정하려고 하면 MY_WINE_NOT_FOUND 예외 발생")
    @Test
    void updateMyWineByWrongMyWine() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        MyWineUpdateRequest myWineUpdate = createMyWineUpdateRequest(LocalDate.parse("2025-01-11"), 200000);

        // when & then
        assertThatThrownBy(() -> myWineService.updateMyWine(-1L, myWineUpdate, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MY_WINE_NOT_FOUND.getMessage());
    }

    @DisplayName("없는 멤버가 보유와인을 수정하려고 하면 MEMBER_NOT_FOUND 예외 발생")
    @Test
    void updateMyWineByWrongMember() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        Long myWineId = saveMyWine(member, wine, LocalDate.parse("2024-12-11"), 100000);
        MyWineUpdateRequest myWineUpdate = createMyWineUpdateRequest(LocalDate.parse("2025-01-11"), 200000);

        // when & then
        assertThatThrownBy(() -> myWineService.updateMyWine(myWineId, myWineUpdate, "wrongMember"))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("다른 멤버의 보유와인을 수정하려고 하면 MY_WINE_UNAUTHORIZED 예외 발생")
    @Test
    void updateMyWineByWrongMember2() {
        // given
        Member member1 = createMember("user");
        memberRepository.save(member1);
        Member member2 = createMember("user2");
        memberRepository.save(member2);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        Long myWineId = saveMyWine(member1, wine, LocalDate.parse("2024-12-11"), 100000);
        MyWineUpdateRequest myWineUpdate = createMyWineUpdateRequest(LocalDate.parse("2025-01-11"), 200000);

        // when & then
        assertThatThrownBy(() -> myWineService.updateMyWine(myWineId, myWineUpdate, member2.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MY_WINE_UNAUTHORIZED.getMessage());
    }

    @DisplayName("특정 보유와인을 삭제한다.")
    @Test
    void deleteMyWineById() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        Long myWineId = saveMyWine(member, wine, LocalDate.parse("2024-12-11"), 100000);

        // when
        myWineService.deleteMyWineById(myWineId, member.getUsername());

        // then
        Optional<MyWine> deletedMyWine = myWineRepository.findById(myWineId);
        assertThat(deletedMyWine).isEmpty();
    }

    @DisplayName("없는 보유와인을 삭제하려고 하면 MY_WINE_NOT_FOUND 예외 발생")
    @Test
    void deleteWineWishlistByIdByWrongMyMyWine() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> myWineService.deleteMyWineById(-1L, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MY_WINE_NOT_FOUND.getMessage());
    }

    @DisplayName("없는 멤버가 보유와인을 삭제하려고 하면 MEMBER_NOT_FOUND 예외 발생")
    @Test
    void deleteMyWineByIdByWrongMember() {
        // given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        Long myWineId = saveMyWine(member, wine, LocalDate.parse("2024-12-11"), 100000);

        // when & then
        assertThatThrownBy(() -> myWineService.deleteMyWineById(myWineId, "wrongMember"))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("다른 멤버의 보유와인을 삭제하려고 하면 MY_WINE_UNAUTHORIZED 예외 발생")
    @Test
    void deleteMyWineByIdByWrongMember2() {
        // given
        Member member1 = createMember("user");
        memberRepository.save(member1);
        Member member2 = createMember("user2");
        memberRepository.save(member2);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        Long myWineId = saveMyWine(member1, wine, LocalDate.parse("2024-12-11"), 100000);

        // when & then
        assertThatThrownBy(() -> myWineService.deleteMyWineById(myWineId, member2.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MY_WINE_UNAUTHORIZED.getMessage());
    }

    private Member createMember(String username) {
        return Member.builder()
                .username(username)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .build();
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

    private Long saveMyWine(Member member, Wine wine, LocalDate purchaseDate, int purchasePrice) {
        return myWineRepository.save(MyWine.builder()
                .member(member)
                .wine(wine)
                .purchaseDate(purchaseDate)
                .purchasePrice(purchasePrice)
                .build()).getId();
    }

    private MyWineRequest createMyWineRequest(Long wineId, LocalDate purchaseDate, int purchasePrice) {
        return MyWineRequest.builder()
                .wineId(wineId)
                .purchaseDate(purchaseDate)
                .purchasePrice(purchasePrice)
                .build();
    }

    private MyWineUpdateRequest createMyWineUpdateRequest(LocalDate purchaseDate, int purchasePrice) {
        return MyWineUpdateRequest.builder()
                .purchaseDate(purchaseDate)
                .purchasePrice(purchasePrice)
                .build();
    }

}