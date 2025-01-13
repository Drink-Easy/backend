package com.drinkeg.drinkeg.domain.wineWishlist.repository;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WineWishlistRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private WineWishlistRepository wineWishlistRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineRepository wineRepository;

    @DisplayName("특정 멤버와 와인으로 위시리스트를 조회한다.")
    @Test
    void findWineWishlistByMemberAndWine() {
        //given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        WineWishlist wineWishlist = wineWishlistRepository.save(WineWishlist.create(member, wine));

        //when
        Optional<WineWishlist> findWineWishlist = wineWishlistRepository.findWineWishlistByMemberAndWine(member, wine);

        //then
        assertThat(findWineWishlist.get()).isEqualTo(wineWishlist);
    }


    @DisplayName("특정 멤버와 와인에 대한 위시리스트가 없는 경우 Optional Empty를 반환한다.")
    @Test
    void findWineWishlistByMemberAndWine_notExists() {
        //given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);

        //when
        Optional<WineWishlist> findWineWishlist = wineWishlistRepository.findWineWishlistByMemberAndWine(member, wine);

        //then
        assertThatThrownBy(findWineWishlist::get).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("특정 멤버와 와인으로 위시리스트가 존재하는지 확인한다.")
    @Test
    void existsByMemberAndWine() {
        //given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);
        wineWishlistRepository.save(WineWishlist.create(member, wine));

        //when
        boolean existsByMemberAndWine = wineWishlistRepository.existsByMemberAndWine(member, wine);

        //then
        assertThat(existsByMemberAndWine).isTrue();
    }


    @DisplayName("특정 멤버와 와인으로 위시리스트가 존재하지 않는지 확인한다.")
    @Test
    void existsByMemberAndWine_notExists() {
        //given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine = createWine("와인");
        wineRepository.save(wine);

        //when
        boolean existsByMemberAndWine = wineWishlistRepository.existsByMemberAndWine(member, wine);

        //then
        assertThat(existsByMemberAndWine).isFalse();
    }

    @DisplayName("특정 멤버의 위시리스트를 생성일자 내림차순으로 조회한다.")
    @Test
    void findByMemberOrderByCreatedAtDesc() {
        //given
        Member member = createMember("user");
        memberRepository.save(member);
        WineWishlist wineWishlist1 = createWineWishlist(member, "와인1");
        WineWishlist wineWishlist2 = createWineWishlist(member, "와인2");
        WineWishlist wineWishlist3 = createWineWishlist(member, "와인3");

        //when
        List<WineWishlist> wineWishlistsByMemberOrderByCreatedAtDesc = wineWishlistRepository.findByMemberOrderByCreatedAtDesc(member);

        //then
        assertThat(wineWishlistsByMemberOrderByCreatedAtDesc).hasSize(3)
                .isEqualTo(List.of(wineWishlist3, wineWishlist2, wineWishlist1));
    }

    @DisplayName("위시리스트가 없는 경우 빈 리스트를 반환한다.")
    @Test
    void findByMemberOrderByCreatedAtDesc_empty() {
        //given
        Member member = createMember("user");
        memberRepository.save(member);

        //when
        List<WineWishlist> wineWishlistsByMemberOrderByCreatedAtDesc = wineWishlistRepository.findByMemberOrderByCreatedAtDesc(member);

        //then
        assertThat(wineWishlistsByMemberOrderByCreatedAtDesc).isEmpty();
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
                .variety("샤도네이")
                .vivinoRating(4.1f)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(10000).build();
    }

    private WineWishlist createWineWishlist(Member member, String wineName) {
        Wine wine = createWine(wineName);
        wineRepository.save(wine);
        return wineWishlistRepository.save(WineWishlist.create(member, wine));
    }

}