package com.drinkeg.drinkeg.wine.service;

import com.drinkeg.drinkeg.S3.S3Service;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.wine.domain.Wine;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeResponseDTO;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.wine.repository.WineRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineWishlistService.WineWishlistService;
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

    private final S3Service s3Service;

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
    public WineResponseDTO getWineResponseByWineId(Long wineId){
        return wineRepository.findWineResponseByWineId(wineId);
    }

    @Override
    public List<WineReviewResponseDTO> getWineReviewsByWineId(Long wineId){
        return wineRepository.findWineReviewsById(wineId);
    }

    @Override
    public HomeResponseDTO getHomeResponse(PrincipalDetail principalDetail) {
        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(principalDetail.getUsername()).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<RecommendWineDTO> recommendWines = wineRepository.findRecommendWines(member);
        return WineConverter.toHomeResponseDTO(member, recommendWines);
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
                    String imageUrl = s3Service.SaveImage(multipartFile);
                    wine.updateImageUrl(imageUrl);
                    wineRepository.save(wine);
                }
            }
        }
    }
}
