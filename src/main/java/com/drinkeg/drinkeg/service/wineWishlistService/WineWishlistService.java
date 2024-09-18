package com.drinkeg.drinkeg.service.wineWishlistService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.WineWishlistDTO.request.WineWishlistRequestDTO;
import com.drinkeg.drinkeg.dto.WineWishlistDTO.response.WineWishlistResponseDTO;

import java.util.List;

public interface WineWishlistService {
    WineWishlistResponseDTO createWineWishlist(WineWishlistRequestDTO wineWishlistRequestDTO, String username);

    List<WineWishlistResponseDTO> getAllWineWishlistByMember(String username);

    void deleteWineWishlistById(Long wineWishlistId, String username);

    public boolean findWineWishByMemberAndWine(Member member, Wine wine);
}
