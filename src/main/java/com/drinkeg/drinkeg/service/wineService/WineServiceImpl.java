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
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        // 와인 이름으로 와인을 검색한다.
        List<Wine> exactMatchWines = wineRepository.findAllByName(searchName);

        // 와인 이름을 공백으로 나누어 검색한다.
        String[] keywords = searchName.split(" ");
        Set<Wine> searchWines = new LinkedHashSet<>(exactMatchWines);
        for(String keyword : keywords) {
            List<Wine> keywordContainingWines = wineRepository.findAllByNameContainingIgnoreCase(keyword);
            // 와인 이름이 포함된 와인을 추가한다.
            if(!keywordContainingWines.isEmpty()){
                searchWines.addAll(keywordContainingWines);
            }

        }

        // 와인을 NoteWineResponseDTO로 변환한다.
        return searchWines.stream().map(WineConverter::toSearchWineDTO).collect(Collectors.toList());
    }

    @Override
    public Wine findWineById(Long wineId) {

        return wineRepository.findById(wineId).orElseThrow(()
                    -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

    }

    @Override
    public HomeResponseDTO getHomeResponse(Member member) {

        List<String> wineSortList = member.getWineSort();
        int monthPriceMax = Math.toIntExact(member.getMonthPriceMax());
        List<String> wineAreaList = member.getWineArea();

        // 와인 종류로 검색한다.
        Set<Wine> searchWines = new LinkedHashSet<>();
        for(String wineSort : wineSortList) {
            List<Wine> sortContainingWines = wineRepository.findAllBySortContainingIgnoreCase(wineSort);
            // 와인 이름이 포함된 와인을 추가한다.
            if(!sortContainingWines.isEmpty()){
                searchWines.addAll(sortContainingWines);
            }
        }
        // 와인 생산지로 검색한다
        for(String wineArea : wineAreaList) {
            List<Wine> areaContainingWines = wineRepository.findAllBySortContainingIgnoreCase(wineArea);
            // 와인 이름이 포함된 와인을 추가한다.
            if(!areaContainingWines.isEmpty()){
                searchWines.addAll(areaContainingWines);
            }
        }

        // 가격으로 필터링 한 후 5개 추출
        List<RecommendWineDTO> recommendWineDTOs = searchWines.stream()
                .filter(wine -> wine.getPrice() <= monthPriceMax)
                .map(WineConverter::toRecommendWineDTO)
                .limit(5)
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
