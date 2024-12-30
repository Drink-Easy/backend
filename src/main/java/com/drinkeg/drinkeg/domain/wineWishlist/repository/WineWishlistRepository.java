package com.drinkeg.drinkeg.domain.wineWishlist.repository;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WineWishlistRepository extends JpaRepository<WineWishlist, Long>, WineWishlistRepositoryCustom {

    WineWishlist findWineWishlistByMemberAndWine(Member member, Wine wine);

    Boolean existsByMemberAndWine(Member member, Wine wine);

    List<WineWishlist> findByMemberOrderByCreatedAtDesc(Member member);

}
