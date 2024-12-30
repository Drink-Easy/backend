package com.drinkeg.drinkeg.wineWishlist.service;

import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;

import java.util.List;

public interface WineWishlistService {

    void createWineWishlist(Long wineId, String username);

    List<SearchWineResponseDTO> getAllWineWishlistByMember(String username);

    void deleteWineWishlistById(Long wineWishlistId, String username);
}
