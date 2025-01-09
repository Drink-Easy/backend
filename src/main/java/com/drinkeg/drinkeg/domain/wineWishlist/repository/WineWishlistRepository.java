package com.drinkeg.drinkeg.domain.wineWishlist.repository;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface WineWishlistRepository extends JpaRepository<WineWishlist, Long>, WineWishlistRepositoryCustom {

    Optional<WineWishlist> findWineWishlistByMemberAndWine(Member member, Wine wine);

    Boolean existsByMemberAndWine(Member member, Wine wine);

    @Query("SELECT w FROM WineWishlist w JOIN FETCH w.wine WHERE w.member = :member ORDER BY w.createdAt DESC")
    List<WineWishlist> findByMemberOrderByCreatedAtDesc(Member member);

}
