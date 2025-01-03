package com.drinkeg.drinkeg.domain.myWine.service;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.domain.MyWine;
import com.drinkeg.drinkeg.domain.myWine.repository.MyWineRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MyWineServiceTest {

    @Autowired
    private MyWineService myWineService;

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MyWineRepository myWineRepository;

    @DisplayName("Member, Wine, MyWineRequest가 모두 올바른 경우 보유와인 생성 확인")
    @Test
    void 올바른Request로들어온요청() {
        // given
        Member member = new Member();
        memberRepository.save(member);

        Wine wine = new Wine();
        wineRepository.save(wine);

        LocalDate localDate = LocalDate.now();
        int price = 100000;

        MyWineRequest myWineRequest = MyWineRequest.builder()
                .wineId(wine.getId())
                .purchaseDate(localDate)
                .purchasePrice(price)
                .build();

        // when
        Long myWineId = myWineService.saveMyWine(myWineRequest, member.getUsername());

        // then
        Optional<MyWine> foundMyWine = myWineRepository.findById(myWineId);
        Long foundId = foundMyWine.get().getId();
        Assertions.assertThat(foundId).isEqualTo(foundId);

    }



}