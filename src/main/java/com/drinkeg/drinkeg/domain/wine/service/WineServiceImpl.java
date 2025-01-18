package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.dto.response.*;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wine.repository.dto.WineNoteStatisticsAvgDto;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class WineServiceImpl implements WineService {
    private final WineRepository wineRepository;
    private final MemberRepository memberRepository;

    private final WineWishlistRepository wineWishlistRepository;
    private final TastingNoteRepository tastingNoteRepository;

    @Override
    public PageResponse<WinePreviewResponse> searchWinesByName(String searchName, Pageable pageable) {
        Page<WinePreviewResponse> winePreviewResponses =
                wineRepository.searchByNameWithPaging(searchName, pageable).map(WinePreviewResponse::of);
        return PageResponse.of(winePreviewResponses);
    }

    @Override
    public void updateWineNoteStatics(Long wineId) {
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        WineNoteStatisticsAvgDto avgDto = tastingNoteRepository.findWineNoteStatisticsByWineId(wineId);
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
    public List<WineReviewResponse> getWineReviewsAndIsLikedByWineId(Long wineId, SortType sortType, Pageable pageable){
        if (!wineRepository.existsById(wineId))
            throw new GeneralException(ErrorStatus.WINE_NOT_FOUND);

        List<TastingNote> tastingNoteList = tastingNoteRepository.findAllTastingNoteBy(wineId, sortType, pageable);

        return tastingNoteList.stream()
                .map(WineReviewResponse::of)
                .toList();
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
