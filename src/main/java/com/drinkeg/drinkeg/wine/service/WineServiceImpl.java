package com.drinkeg.drinkeg.wine.service;

import com.drinkeg.drinkeg.dto.HomeDTO.HomeWineDTO;
import com.drinkeg.drinkeg.storageService.StoragePathName;
import com.drinkeg.drinkeg.storageService.StorageService;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.wineWishlist.repository.WineWishlistRepository;
import com.drinkeg.drinkeg.wine.domain.Wine;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.wine.repository.WineRepository;
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
    public List<WineReviewResponseDTO> getWineReviewsAndIsLikedByWineId(Long wineId, boolean orderByLatest){

        return wineRepository.findWineReviewsByWineIdAndMemberId(wineId, orderByLatest);
    }

    // 회원 닉네임과 추천와인 10개 반환
    @Override
    public List<HomeWineDTO> getRecommendWineList(PrincipalDetail principalDetail) {
        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(principalDetail.getUsername()).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // max 20개의 추천 와인을 찾는다.
        List<HomeWineDTO> recommendWines = wineRepository.findRecommendWinesByMember(member);

        // 만약 추천 와인의 수가 10개를 넘어간다면, 랜덤으로 10개의 와인만 반환한다.
        if (recommendWines.size() > 10) {
            Collections.shuffle(recommendWines);
            recommendWines = recommendWines.subList(0, 10);
        }

        return recommendWines;
    }

    @Override
    public List<HomeWineDTO> getMostLikedWineList() {

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
