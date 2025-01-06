package com.drinkeg.drinkeg.domain.wineWishlist.service;


import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;

import java.util.List;

public interface WineWishlistService {

    void createWineWishlist(Long wineId, String username);

    List<WinePreviewResponse> getAllWineWishlistByMember(String username);

    void deleteWineWishlistById(Long wineWishlistId, String username);
}
