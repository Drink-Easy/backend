package com.drinkeg.drinkeg.repository;

public interface WineWishlistRepositoryCustom {

    Boolean existsByMemberIdAndWineId(Long memberId, Long wineId);
}
