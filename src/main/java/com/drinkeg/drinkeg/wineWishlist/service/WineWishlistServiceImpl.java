package com.drinkeg.drinkeg.wineWishlist.service;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.wine.domain.Wine;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.wine.repository.WineRepository;
import com.drinkeg.drinkeg.wineWishlist.repository.WineWishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WineWishlistServiceImpl implements WineWishlistService{
    private final WineWishlistRepository wineWishlistRepository;
    private final MemberRepository memberRepository;
    private final WineRepository wineRepository;

    @Override
    public void createWineWishlist(Long wineId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        if (wineWishlistRepository.existsByMemberAndWine(member, wine))
            throw new GeneralException(ErrorStatus.WINE_WISHLIST_ALREADY_EXISTS);

        wineWishlistRepository.save(WineWishlist.create(member, wine));
    }

    @Override
    public List<SearchWineResponseDTO> getAllWineWishlistByMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<WineWishlist> wishlistWineList = wineWishlistRepository.findByMemberOrderByCreatedAtDesc(member);

        return wishlistWineList.stream().map(wineWishlist
                -> SearchWineResponseDTO.create(wineWishlist.getWine(), true)).toList();
    }

    @Override
    public void deleteWineWishlistById(Long wineId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        WineWishlist wineWishlist = wineWishlistRepository.findWineWishlistByMemberAndWine(member, wine);

        if (!wineWishlist.getMember().equals(member))
            throw new GeneralException(ErrorStatus.WINE_WISHLIST_UNAUTHORIZED);

        wineWishlistRepository.deleteById(wineWishlist.getId());
    }
}
