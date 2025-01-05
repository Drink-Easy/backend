package com.drinkeg.drinkeg.domain.wineWishlist.service;

import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<WinePreviewResponse> getAllWineWishlistByMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<WineWishlist> wishlistWineList = wineWishlistRepository.findByMemberOrderByCreatedAtDesc(member);

        return wishlistWineList.stream().map(wineWishlist
                -> WinePreviewResponse.of(wineWishlist.getWine())).toList();
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
