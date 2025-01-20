package com.drinkeg.drinkeg.domain.myWine.repository;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWine;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MyWineRepositoryTest extends IntegrationTestSupport {

    @Autowired
    MyWineRepository myWineRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineRepository wineRepository;

    @DisplayName("특정 멤버의 전체 보유와인을 최신 생성 조회한다.")
    @Test
    void findByMemberOrderByCreatedAt() {
        //given
        Member member = createMember("user");
        memberRepository.save(member);
        Wine wine1 = createWine("wine1");
        Wine wine2 = createWine("wine2");
        Wine wine3 = createWine("wine3");
        wineRepository.saveAll(Arrays.asList(wine1, wine2, wine3));

        MyWine myWine1 = createMyWine(member, wine1, LocalDate.parse("2025-01-01"), 10000);
        MyWine myWine2 = createMyWine(member, wine2, LocalDate.parse("2025-01-02"), 20000);
        MyWine myWine3 = createMyWine(member, wine3, LocalDate.parse("2025-01-03"), 30000);
        myWineRepository.saveAll(Arrays.asList(myWine1, myWine2, myWine3));


        //when
        List<MyWine> myWineList = myWineRepository.findByMemberOrderByCreatedAt(member);

        //then
        Assertions.assertThat(myWineList).hasSize(3)
                .isEqualTo(List.of(myWine3, myWine2, myWine1));
    }

    @DisplayName("특정 멤버의 보유 와인이 없는 경우 빈 리스트를 반환한다.")
    @Test
    void findByMemberOrderByCreatedAt_notExists() {
        //given
        Member member = createMember("user");
        memberRepository.save(member);

        //when
        List<MyWine> myWineList = myWineRepository.findByMemberOrderByCreatedAt(member);

        //then
        assertThat(myWineList).isEqualTo(new ArrayList<>());
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

    private MyWine createMyWine(Member member, Wine wine, LocalDate purchaseDate, int purchasePrice) {
        return MyWine.create(member, wine, purchaseDate, purchasePrice);
    }
}