package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.wine.domain.Wine;
import com.drinkeg.drinkeg.wineWishlist.domain.WineWishlist;
import com.drinkeg.drinkeg.wineWishlist.dto.response.WineWishlistResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;

public class WineWishlistConverter {
    public static WineWishlistResponseDTO toWineWishlistResponseDTO(WineWishlist wineWishlist) {
        return WineWishlistResponseDTO.builder()
                .id(wineWishlist.getId())
                .wine(SearchWineResponseDTO.create(wineWishlist.getWine(), true))
                .build();
    }

    public static WineWishlist toWineWishlist(Wine wine, Member member) {
        return WineWishlist.builder()
                .wine(wine)
                .member(member)
                .build();
    }
}
