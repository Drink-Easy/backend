package com.drinkeg.drinkeg.domain.wineWishlist.service;

import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wine.wineVintage.repository.WineVintageRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WineWishlistServiceImpl implements WineWishlistService{
    private final WineWishlistRepository wineWishlistRepository;
    private final MemberRepository memberRepository;
    private final WineVintageRepository wineVintageRepository;

    @Override
    public Long createWineWishlist(Long wineId, Integer vintageYear, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (vintageYear == null) vintageYear = 0;
        WineVintage wineVintage = wineVintageRepository.findByWineIdAndVintageYear(wineId, vintageYear);
        if(wineVintage == null) throw new GeneralException(ErrorStatus.WINE_NOT_FOUND);

        if (wineWishlistRepository.existsByMemberAndWineVintage(member, wineVintage))
            throw new GeneralException(ErrorStatus.WINE_WISHLIST_ALREADY_EXISTS);

        WineWishlist wineWishlist = wineWishlistRepository.save(WineWishlist.create(member, wineVintage));
        return wineWishlist.getId();
    }

    @Override
    public List<WinePreviewResponse> getAllWineWishlistByMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<WineWishlist> wishlistWineList = wineWishlistRepository.findByMemberOrderByCreatedAtDesc(member);

        return wishlistWineList.stream().map(wineWishlist
                -> WinePreviewResponse.of(wineWishlist.getWineVintage())).toList();
    }

    @Override
    public void deleteWineWishlist(Long wineId, Integer vintageYear,String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (vintageYear == null) vintageYear = 0;
        WineVintage wineVintage = wineVintageRepository.findByWineIdAndVintageYear(wineId, vintageYear);
        if(wineVintage == null) throw new GeneralException(ErrorStatus.WINE_NOT_FOUND);

        WineWishlist wineWishlist = wineWishlistRepository.findWineWishlistByMemberAndWineVintage(member, wineVintage).orElseThrow(
                () -> new GeneralException(ErrorStatus.WINE_WISHLIST_NOT_FOUND));

        wineWishlistRepository.deleteById(wineWishlist.getId());
    }
}
