package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineWishlist;
import com.drinkeg.drinkeg.dto.WineWishlistDTO.response.WineWishlistResponseDTO;

public class WineWishlistConverter {
    public static WineWishlistResponseDTO toWineWishlistResponseDTO(WineWishlist wineWishlist) {
        return WineWishlistResponseDTO.builder()
                .id(wineWishlist.getId())
                .wine(WineConverter.toSearchWineResponseDTO(wineWishlist.getWine()))
                .build();
    }

    public static WineWishlist toWineWishlist(Wine wine, Member member) {
        return WineWishlist.builder()
                .wine(wine)
                .member(member)
                .build();
    }
}
