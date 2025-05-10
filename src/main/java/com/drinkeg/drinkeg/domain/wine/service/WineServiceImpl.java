package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wine.dto.WineNoteStatisticsAvgDto;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class WineServiceImpl implements WineService {
    private final WineRepository wineRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;

    private final WineWishlistRepository wineWishlistRepository;
    private final TastingNoteRepository tastingNoteRepository;

    @Override
    public PageResponse<WinePreviewResponse> searchWinesByName(String searchName, Pageable pageable) {

        String cleanSearchName = searchName.replace(" ", "").toLowerCase();
        List<WinePreviewResponse> winePreviewList =
                wineRepository.searchByName(cleanSearchName, pageable)
                        .stream()
                        .map(WinePreviewResponse::of)
                        .toList();
        long total = wineRepository.countSearchWine(cleanSearchName);
        return PageResponse.of(new PageImpl<>(winePreviewList, pageable, total));
    }

    @Override
    public void updateWineNoteStatics(Long wineId) {
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        WineNoteStatisticsAvgDto avgDto = tastingNoteRepository.findWineStatisticsByWineId(wineId);
        List<String> topThreeNose = tastingNoteRepository.findTopThreeNoseByWineId(wineId);

        wine.getWineNoteStatistics()
                .updateAvgStatistics(avgDto)
                .updateNose(topThreeNose);
    }

    @Override
    public WineWithThreeReviewsResponse getWineInfoWithThreeReviews(Long wineId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));
        boolean isLiked = wineWishlistRepository.existsByMemberAndWine(member, wine);
        List<TastingNote> recentThreeTastingNote = tastingNoteRepository.findRecentThreeTastingNoteBy(wineId);

        return WineWithThreeReviewsResponse.of(wine, recentThreeTastingNote, isLiked);
    }

    @Override
    public PageResponse<WineReviewResponse> getWineReviewsAndIsLikedByWineId(Long wineId, SortType sortType, Pageable pageable){
        if (!wineRepository.existsById(wineId))
            throw new GeneralException(ErrorStatus.WINE_NOT_FOUND);

        List<WineReviewResponse> wineReviewResponseList = tastingNoteRepository.findAllTastingNoteBy(wineId, sortType, pageable).stream()
                .map(WineReviewResponse::of)
                .toList();
        long total = tastingNoteRepository.countTastingNoteByWineId(wineId);

        return PageResponse.of(new PageImpl<>(wineReviewResponseList, pageable, total));
    }

    @Override
    public List<HomeWineResponse> getRecommendWineList(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Wine> recommendWines = wineRepository.findRecommendWinesBy(member.getWineArea(), member.getWineSort(), member.getMonthPriceMax());

        Collections.shuffle(recommendWines);
        recommendWines = recommendWines.subList(0, Math.min(recommendWines.size(), 10));

        return recommendWines.stream()
                .map(HomeWineResponse::of)
                .toList();
    }

    @Override
    public List<HomeWineResponse> getMostLikedWineList() {
        List<Wine> mostLikedWines = wineRepository.findMostLikedWines();
        return mostLikedWines.stream()
                .map(HomeWineResponse::of)
                .toList();
    }
}
