package com.drinkeg.drinkeg.service.wineService;

import com.drinkeg.drinkeg.S3.S3Service;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeResponseDTO;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineRepository;
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
    private final S3Service s3Service;

    @Override
    public List<SearchWineResponseDTO> searchWinesByName(String searchName) {

        // 검색한 와인 이름이 포함된 모든 와인을 찾는다 (LIKE '%검색어%').
        List<Wine> foundWines = wineRepository.findAllByNameContainingIgnoreCase(searchName);

        // 와인을 SearchWineResponseDTO로 변환하여 반환한다.
        return foundWines.stream()
                .map(WineConverter::toSearchWineDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Wine findWineById(Long wineId) {

        return wineRepository.findById(wineId).orElseThrow(()
                    -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

    }

    @Override
    public HomeResponseDTO getHomeResponse(Member member) {

        List<String> wineSortList = member.getWineSort();
        List<String> wineAreaList = member.getWineArea();
        int monthPriceMax = (int) ((member.getMonthPriceMax())/1300);

        Map<Wine, Double> wineScoreMap = new HashMap<>();

        // 와인 종류로 검색하여 가중치 부여
        for (String wineSort : wineSortList) {
            List<Wine> sortContainingWines = wineRepository.findAllBySortContainingIgnoreCase(wineSort);
            for (Wine wine : sortContainingWines) {
                // 가중치 0.2 부여
                wineScoreMap.put(wine, wineScoreMap.getOrDefault(wine, 0.0) + 0.2);
            }
        }

        // 와인 생산지로 검색하여 가중치 부여
        for (String wineArea : wineAreaList) {
            List<Wine> areaContainingWines = wineRepository.findAllByAreaContainingIgnoreCase(wineArea);
            for (Wine wine : areaContainingWines) {
                // 가중치 0.2 부여
                wineScoreMap.put(wine, wineScoreMap.getOrDefault(wine, 0.0) + 0.2);
            }
        }

        // 와인 평점을 최종 가중치에 반영
        wineScoreMap.replaceAll((wine, score) -> score + wine.getRating());

        // 가격으로 필터링, 가중치로 정렬, 상위 20개 추출
        List<Wine> topWines = new ArrayList<>(wineScoreMap.entrySet().stream()
                .filter(entry -> entry.getKey().getPrice() <= monthPriceMax)
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))  // 가중치로 정렬
                .map(Map.Entry::getKey)
                .limit(10)
                .toList());

        // 상위 20개 중에서 랜덤으로 5개 선택
        Collections.shuffle(topWines, new Random());
        List<RecommendWineDTO> recommendWineDTOs = topWines.stream()
                .limit(5)
                .map(WineConverter::toRecommendWineDTO)
                .toList();

        return WineConverter.toHomeResponseDTO(member, recommendWineDTOs);
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
