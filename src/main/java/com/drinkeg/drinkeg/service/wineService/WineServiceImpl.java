package com.drinkeg.drinkeg.service.wineService;

import com.drinkeg.drinkeg.S3.S3Service;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
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
        List<SearchWineResponseDTO> collectWines = searchWines.stream().map(wine ->
                WineConverter.toNoteSearchWineDTO(wine)).collect(Collectors.toList());

        return collectWines;
    }

    @Override
    public Wine findWineById(Long wineId) {

        return wineRepository.findById(wineId).orElseThrow(()
                    -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

    }

    @Override
    public List<RecommendWineDTO> recommendWine(Member member) {

        List<String> wineSort = member.getWineSort();
        Long monthPriceMax = member.getMonthPriceMax();
        List<String> wineArea = member.getWineArea();
        List<String> wineVariety = member.getWineVariety();

        return List.of();
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
