package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wine.repository.dto.WineNoteStatisticsAvgDto;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineWithThreeReviewsResponse;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
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
    private final StorageService storageService;

    @Override
    public List<WinePreviewResponse> searchWinesByName(String searchName) {
        List<Wine> searchWines = wineRepository.findAllByNameContainingIgnoreCaseOrderByName(searchName);

        return searchWines.stream()
                .map(WinePreviewResponse::of)
                .toList();
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
    public List<WineReviewResponse> getWineReviewsAndIsLikedByWineId(Long wineId, SortType sortType){
        if (!wineRepository.existsById(wineId))
            throw new GeneralException(ErrorStatus.WINE_NOT_FOUND);

        List<TastingNote> tastingNoteList = tastingNoteRepository.findAllTastingNoteBy(wineId, sortType);

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
        recommendWines = recommendWines.subList(0, 10);

        return recommendWines.stream()
                .map(HomeWineResponse::of)
                .toList();
    }

    @Override
    public List<HomeWineResponse> getMostLikedWineList() {
        return wineRepository.findMostLikedWines();
    }

    @Override
    public void uploadWineImage() throws IOException {
        List<Wine> wines = wineRepository.findAll();

        for (Wine wine : wines) {
            if (wine.getImageUrl() == null) {
                String imageName = wine.getName().toLowerCase().replace("'", "").replace(" ", "-") + ".jpg";
                File imageFile = new File(System.getenv("IMAGE_PATH")+ imageName);

                if (imageFile.exists()) {
                    MultipartFile multipartFile = new CustomMultipartFile(imageFile);
                    String imageUrl = storageService.uploadFile(multipartFile, StoragePathName.WINE);
                    wine.updateImageUrl(imageUrl);
                    wineRepository.save(wine);
                }
            }
        }
    }
}
