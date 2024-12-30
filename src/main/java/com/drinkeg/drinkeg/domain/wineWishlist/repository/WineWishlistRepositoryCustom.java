package com.drinkeg.drinkeg.domain.wineWishlist.repository;

public interface WineWishlistRepositoryCustom {

    Boolean existsByMemberIdAndWineId(Long memberId, Long wineId);
}
