package com.drinkeg.drinkeg.wineWishlist.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.wine.domain.Wine;
import com.drinkeg.drinkeg.wineWishlist.domain.WineWishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WineWishlistRepository extends JpaRepository<WineWishlist, Long>, WineWishlistRepositoryCustom {

    List<WineWishlist> findAllByMember(Member member);

    Boolean existsByMemberAndWine(Member member, Wine wine);

}
