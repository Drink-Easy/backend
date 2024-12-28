package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wineWishlist.repository.WineWishlistRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeResponseDTO;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
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
    public List<SearchWineResponseDTO> searchWinesByName(String searchName, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(principalDetail.getUsername()).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // 검색한 와인 이름이 포함된 모든 와인을 찾는다 (LIKE '%검색어%').
        // 이때 memberId를 이용해 isLiked()를 같이 조회한다
        return wineRepository.findWinesWithLikeStatus(searchName, member.getId());
    }

    @Override
    public Wine findWineById(Long wineId) {
        return wineRepository.findById(wineId).orElseThrow(()
                    -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));
    }

    @Override
    public WineResponseWithThreeReviewsDTO getWineResponseByWineId(Long wineId, PrincipalDetail principalDetail){
        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(principalDetail.getUsername()).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        return wineRepository.findWineResponseByWineId(wineId, member.getId());
    }

    @Override
    public WineReviewResponseDTO getWineReviewsAndIsLikedByWineId(Long wineId, PrincipalDetail principalDetail, boolean orderByLatest){
        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(principalDetail.getUsername()).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<WineReviewDTO> wineReviews = wineRepository.findWineReviewsByWineIdAndMemberId(wineId, orderByLatest);
        boolean liked = wineWishlistRepository.existsByMemberIdAndWineId(member.getId(), wineId);

        return WineReviewResponseDTO.create(wineReviews, liked);
    }

    @Override
    public HomeResponseDTO getHomeResponse(PrincipalDetail principalDetail) {
        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(principalDetail.getUsername()).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // max 20개의 추천 와인을 찾는다.
        List<RecommendWineDTO> recommendWines = wineRepository.findRecommendWines(member);

        // 만약 추천 와인의 수가 5개를 넘어간다면, 랜덤으로 5개의 와인만 반환한다.
        if (recommendWines.size() > 5) {
            Collections.shuffle(recommendWines);
            recommendWines = recommendWines.subList(0, 5);
        }
        return HomeResponseDTO.create(member, recommendWines);
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
