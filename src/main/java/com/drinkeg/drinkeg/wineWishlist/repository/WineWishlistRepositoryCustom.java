package com.drinkeg.drinkeg.wineWishlist.repository;

public interface WineWishlistRepositoryCustom {

    Boolean existsByMemberIdAndWineId(Long memberId, Long wineId);
}
