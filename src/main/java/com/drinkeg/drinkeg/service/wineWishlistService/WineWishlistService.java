package com.drinkeg.drinkeg.service.wineWishlistService;

import com.drinkeg.drinkeg.dto.WineWishlistDTO.request.WineWishlistRequestDTO;
import com.drinkeg.drinkeg.dto.WineWishlistDTO.response.WineWishlistResponseDTO;

import java.util.List;

public interface WineWishlistService {
    WineWishlistResponseDTO createWineWishlist(WineWishlistRequestDTO wineWishlistRequestDTO, String username);

    List<WineWishlistResponseDTO> getAllWineWishlistByMember(String username);

    void deleteWineWishlistById(Long wineWishlistId, String username);
}
