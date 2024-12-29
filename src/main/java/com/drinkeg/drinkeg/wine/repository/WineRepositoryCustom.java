package com.drinkeg.drinkeg.wine.repository;

import com.drinkeg.drinkeg.dto.HomeDTO.HomeWineDTO;
import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewResponseDTO;

import java.util.List;

public interface WineRepositoryCustom {
    List<SearchWineResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId);
    WineResponseWithThreeReviewsDTO findWineResponseByWineId(Long wineId, Long memberID);

    List<WineReviewResponseDTO> findWineReviewsByWineIdAndMemberId(Long wineId, boolean orderByLatest);

    List<HomeWineDTO> findRecommendWinesByMember(Member member);

    List<HomeWineDTO> findMostLikedWines();

    public List<SearchWineResponseDTO> findWishlistWinesByUsername(String username);
}
