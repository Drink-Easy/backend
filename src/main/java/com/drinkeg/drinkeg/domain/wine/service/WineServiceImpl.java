package com.drinkeg.drinkeg.domain.wine.service;

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

        WineNoteStatisticsAvgDto avgDto = wineRepository.findWineNoteStatisticsByWineId(wineId);
        List<String> topThreeNose = wineRepository.findTopThreeNoseByWineId(wineId);

        wine.getWineNoteStatistics()
                .updateAvgStatistics(avgDto)
                .updateNose(topThreeNose);
    }

    @Override
    public WineWithThreeReviewsResponse getWineResponseByWineId(Long wineId, String username){
        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        return wineRepository.findWineResponseByWineId(wineId, member.getId());
    }

    @Override
    public List<WineReviewResponse> getWineReviewsAndIsLikedByWineId(Long wineId, boolean orderByLatest){

        return wineRepository.findWineReviewsByWineIdAndMemberId(wineId, orderByLatest);
    }

    // 추천와인 10개 반환
    @Override
    public List<HomeWineResponse> getRecommendWineList(String username) {
        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // max 20개의 추천 와인을 찾는다.
        List<HomeWineResponse> recommendWines = wineRepository.findRecommendWinesByMember(member);

        // 만약 추천 와인의 수가 10개를 넘어간다면, 랜덤으로 10개의 와인만 반환한다.
        if (recommendWines.size() > 10) {
            Collections.shuffle(recommendWines);
            recommendWines = recommendWines.subList(0, 10);
        }

        return recommendWines;
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
