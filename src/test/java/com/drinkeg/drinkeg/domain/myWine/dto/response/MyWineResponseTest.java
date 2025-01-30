package com.drinkeg.drinkeg.domain.myWine.dto.response;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.myWine.domain.MyWine;
import com.drinkeg.drinkeg.domain.myWine.repository.MyWineRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MyWineResponseTest extends IntegrationTestSupport {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineRepository wineRepository;
    @Autowired
    MyWineRepository myWineRepository;

    @DisplayName("MyWineResponse of 메서드 매개변수로 MyWine이 들어가면 MyWineResponse로 변환한다.")
    @Test
    void MyWineResponseOf() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("와인"));
        Long myWineId = saveMyWine(member, wine, LocalDate.parse("2025-01-01"), 100000);

        // when
        MyWineResponse myWineResponse = MyWineResponse.of(myWineRepository.findById(myWineId).get(), LocalDate.parse("2025-01-15"));

        // then
        Assertions.assertThat(myWineResponse)
                .extracting("myWineId", "wineId", "wineName", "wineSort", "wineCountry", "wineRegion", "wineVariety", "wineImageUrl", "purchaseDate", "purchasePrice", "period")
                .containsExactly(myWineId, wine.getId(), wine.getName(), wine.getSort(), wine.getCountry(), wine.getRegion(), wine.getVariety(), wine.getImageUrl(), LocalDate.parse("2025-01-01"), 100000, 14);
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
}