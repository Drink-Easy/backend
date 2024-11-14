package com.drinkeg.drinkeg.service.wineService;

import com.drinkeg.drinkeg.S3.S3Service;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeResponseDTO;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineWishlistService.WineWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WineServiceImpl implements WineService {
    private final WineRepository wineRepository;

    private final MemberService memberService;
    private final WineWishlistService wineWishlistService;
    private final S3Service s3Service;

    @Override
    public List<SearchWineResponseDTO> searchWinesByName(String searchName, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);


        // 검색한 와인 이름이 포함된 모든 와인을 찾는다 (LIKE '%검색어%').
        List<Wine> foundWines = wineRepository.findAllByNameContainingIgnoreCaseOrderByName(searchName);

        // 와인을 NoteWineResponseDTO로 변환한다.
        return foundWines.stream()
                .map(wine -> WineConverter.toSearchWineResponseDTO(wine,
                        wineWishlistService.isLiked(member, wine))
                )
                .collect(Collectors.toList());
    }

    @Override
    public Wine findWineById(Long wineId) {

        return wineRepository.findById(wineId).orElseThrow(()
                    -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

    }

    public WineResponseDTO getWineResponseByWineId(Long wineId){
        return wineRepository.findWineResponseByWineId(wineId);
    }

    @Override
    public List<WineReviewResponseDTO> getWineReviewsByWineId(Long wineId){
        return wineRepository.findWineReviewsById(wineId);
    }

    @Override
    public HomeResponseDTO getHomeResponse(Member member) {

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
