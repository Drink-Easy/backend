package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineWishlist;
import com.drinkeg.drinkeg.dto.WineWishlistDTO.response.WineWishlistResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WineWishlistRepository extends JpaRepository<WineWishlist, Long> {

    List<WineWishlist> findAllByMember(Member member);

    Boolean existsByMemberAndWine(Member member, Wine wine);

}
