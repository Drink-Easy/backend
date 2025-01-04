package com.drinkeg.drinkeg.wineWishlist.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WineWishlistControllerServiceImplTest extends IntegrationTestSupport {

    @Autowired
    WineWishlistRepository wineWishlistRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void getAllWineWishlistByMember() {

        Member member = new Member();

        memberRepository.save(member);
        List<WineWishlist> wineWishlists = wineWishlistRepository.findByMemberOrderByCreatedAtDesc(member);

        List<WinePreviewResponse> list = wineWishlists.stream()
                .map(wineWishlist -> WinePreviewResponse.create(wineWishlist.getWine()))
                .toList();

        assertEquals(list, new ArrayList<>());
    }
}