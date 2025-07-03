package com.drinkeg.drinkeg.domain.wineWishlist.repository;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wineVintage.repository.WineVintageRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
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
    @Autowired
    private WineVintageRepository wineVintageRepository;

    @DisplayName("특정 멤버와 와인으로 위시리스트를 조회한다.")
    @Test
    void findWineWishlistByMemberAndWineVintage() {
        //given
        Member member = saveMember("user");
        Wine wine = saveWine("와인");
        WineVintage wineVintage = saveWineVintage(wine, 2017);
        WineWishlist wineWishlist = wineWishlistRepository.save(WineWishlist.create(member, wineVintage));

        //when
        Optional<WineWishlist> findWineWishlist = wineWishlistRepository.findWineWishlistByMemberAndWineVintage(member, wineVintage);

        //then
        assertThat(findWineWishlist.get()).isEqualTo(wineWishlist);
    }


    @DisplayName("특정 멤버와 와인에 대한 위시리스트가 없는 경우 Optional Empty를 반환한다.")
    @Test
    void findWineWishlistByMemberAndWine_Vintage_notExists() {
        //given
        Member member = saveMember("user");
        Wine wine = saveWine("와인");
        WineVintage wineVintage = saveWineVintage(wine, 2017);

        //when
        Optional<WineWishlist> findWineWishlist = wineWishlistRepository.findWineWishlistByMemberAndWineVintage(member, wineVintage);

        //then
        assertThat(findWineWishlist).isEmpty();
    }

    @DisplayName("특정 멤버와 와인으로 위시리스트가 존재하는지 확인한다.")
    @Test
    void existsByMemberAndWineVintage() {
        //given
        Member member = saveMember("user");
        Wine wine = saveWine("와인");
        WineVintage wineVintage = saveWineVintage(wine, 2017);
        WineWishlist wineWishlist = wineWishlistRepository.save(WineWishlist.create(member, wineVintage));

        //when
        Optional<WineWishlist> findWineWishlist = wineWishlistRepository.findWineWishlistByMemberAndWineVintage(member, wineVintage);

        //when
        boolean existsByMemberAndWine = wineWishlistRepository.existsByMemberAndWineVintage(member, wineVintage);

        //then
        assertThat(existsByMemberAndWine).isTrue();
    }


    @DisplayName("특정 멤버와 와인으로 위시리스트가 존재하지 않는지 확인한다.")
    @Test
    void existsByMemberAndWine_notExistsVintage() {
        //given
        Member member = saveMember("user");
        Wine wine = saveWine("와인");
        WineVintage wineVintage = saveWineVintage(wine, 2017);

        //when
        boolean existsByMemberAndWine = wineWishlistRepository.existsByMemberAndWineVintage(member, wineVintage);

        //then
        assertThat(existsByMemberAndWine).isFalse();
    }

    @DisplayName("특정 멤버의 위시리스트를 생성일자 내림차순으로 조회한다.")
    @Test
    void findByMemberOrderByCreatedAtDesc() {
        //given
        Member member = saveMember("user");
        Wine wine1 = saveWine("와인1");
        Wine wine2 = saveWine("와인2");
        Wine wine3 = saveWine("와인3");
        WineVintage wineVintage1 = saveWineVintage(wine1, 2017);
        WineVintage wineVintage2 = saveWineVintage(wine2, 2018);
        WineVintage wineVintage3 = saveWineVintage(wine3, 2019);

        WineWishlist wineWishlist1 = saveWineWishlist(member, wineVintage1);
        WineWishlist wineWishlist2 = saveWineWishlist(member, wineVintage2);
        WineWishlist wineWishlist3 = saveWineWishlist(member, wineVintage3);

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
        Member member = saveMember("user");

        //when
        List<WineWishlist> wineWishlistsByMemberOrderByCreatedAtDesc = wineWishlistRepository.findByMemberOrderByCreatedAtDesc(member);

        //then
        assertThat(wineWishlistsByMemberOrderByCreatedAtDesc).isEmpty();
    }

    private Member saveMember(String username) {
        return memberRepository.save(Member.builder()
                .username(username)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .build());
    }

    private Wine saveWine(String name) {
        return wineRepository.save(Wine.builder()
                .name(name)
                .imageUrl("http://default.image")
                .sort("레드")
                .country("프랑스")
                .variety("샤도네이")
                .vivinoRating(4.1f)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(10000).build());
    }

    private WineVintage saveWineVintage(Wine wine, int vintageYear) {
        return wineVintageRepository.save(WineVintage.create(vintageYear, wine));
    }



    private WineWishlist saveWineWishlist(Member member, WineVintage wineVintage) {
        return wineWishlistRepository.save(WineWishlist.create(member, wineVintage));
    }

}