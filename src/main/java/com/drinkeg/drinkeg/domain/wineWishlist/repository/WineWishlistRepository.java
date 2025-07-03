package com.drinkeg.drinkeg.domain.wineWishlist.repository;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface WineWishlistRepository extends JpaRepository<WineWishlist, Long> {

    Optional<WineWishlist> findWineWishlistByMemberAndWineVintage(Member member, WineVintage wineVintage);

    Boolean existsByMemberAndWineVintage(Member member, WineVintage wineVintage);

    @Query("SELECT w FROM WineWishlist w JOIN FETCH w.wineVintage WHERE w.member = :member ORDER BY w.createdAt DESC")
    List<WineWishlist> findByMemberOrderByCreatedAtDesc(Member member);

}
