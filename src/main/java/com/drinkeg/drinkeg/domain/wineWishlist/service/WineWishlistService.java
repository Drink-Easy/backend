package com.drinkeg.drinkeg.domain.wineWishlist.service;


import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;

import java.util.List;

public interface WineWishlistService {

    Long createWineWishlist(Long wineId, Integer vintageYear, String username);

    List<WinePreviewResponse> getAllWineWishlistByMember(String username);

    void deleteWineWishlist(Long wineId, Integer vintageYear, String username);
}
