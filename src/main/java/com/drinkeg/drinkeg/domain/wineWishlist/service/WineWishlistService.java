package com.drinkeg.drinkeg.domain.wineWishlist.service;

import com.drinkeg.drinkeg.domain.wineWishlist.dto.request.WineWishlistRequestDTO;
import com.drinkeg.drinkeg.domain.wineWishlist.dto.response.WineWishlistResponseDTO;

import java.util.List;

public interface WineWishlistService {
    WineWishlistResponseDTO createWineWishlist(WineWishlistRequestDTO wineWishlistRequestDTO, String username);

    List<WineWishlistResponseDTO> getAllWineWishlistByMember(String username);

    void deleteWineWishlistById(Long wineWishlistId, String username);
}
