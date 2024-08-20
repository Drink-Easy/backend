package com.drinkeg.drinkeg.service.wineWishlistService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineWishlistConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineWishlist;
import com.drinkeg.drinkeg.dto.WineWishlistDTO.request.WineWishlistRequestDTO;
import com.drinkeg.drinkeg.dto.WineWishlistDTO.response.WineWishlistResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.WineRepository;
import com.drinkeg.drinkeg.repository.WineWishlistRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineService.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineWishlistServiceImpl implements WineWishlistService{
    private final WineWishlistRepository wineWishlistRepository;
    private final MemberRepository memberRepository;
    private final WineRepository wineRepository;

    @Override
    public WineWishlistResponseDTO createWineWishlist(WineWishlistRequestDTO wineWishlistRequestDTO, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Wine wine = wineRepository.findById(wineWishlistRequestDTO.getWineId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        if (wineWishlistRepository.existsByMemberAndWine(member, wine))
            throw new GeneralException(ErrorStatus.WINE_WISHLIST_ALREADY_EXISTS);

        WineWishlist wineWishlist = wineWishlistRepository.save(WineWishlistConverter.toWineWishlist(wine, member));

        return WineWishlistConverter.toWineWishlistResponseDTO(wineWishlist);
    }

    @Override
    public List<WineWishlistResponseDTO> getAllWineWishlistByMember(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        return wineWishlistRepository.findAllByMember(member).stream()
                .map(WineWishlistConverter::toWineWishlistResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWineWishlistById(Long wineWishlistId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        WineWishlist wineWishlist = wineWishlistRepository.findById(wineWishlistId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_WISHLIST_NOT_FOUND));

        if (!wineWishlist.getMember().getId().equals(member.getId()))
            throw new GeneralException(ErrorStatus.WINE_WISHLIST_UNAUTHORIZED);

        wineWishlistRepository.deleteById(wineWishlistId);
    }
}
