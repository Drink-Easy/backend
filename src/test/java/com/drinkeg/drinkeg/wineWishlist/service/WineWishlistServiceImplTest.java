package com.drinkeg.drinkeg.wineWishlist.service;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.wineWishlist.repository.WineWishlistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class WineWishlistServiceImplTest {

    @Autowired
    WineWishlistRepository wineWishlistRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void getAllWineWishlistByMember() {

        Member member = new Member();

        memberRepository.save(member);
        List<WineWishlist> wineWishlists = wineWishlistRepository.findByMemberOrderByCreatedAtDesc(member);

        List<SearchWineResponseDTO> list = wineWishlists.stream().map(wineWishlist
                -> SearchWineResponseDTO.create(wineWishlist.getWine(), true)).toList();

        assertEquals(list, new ArrayList<>());

    }
}