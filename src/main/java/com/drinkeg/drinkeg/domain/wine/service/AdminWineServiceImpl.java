package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.dto.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.dto.request.WineUpdateRequest;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.AdminWinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.AdminWineResponse;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wine.wineVintage.repository.WineVintageRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminWineServiceImpl implements AdminWineService {
    private final WineRepository wineRepository;
    private final WineVintageRepository wineVintageRepository;
    private final StorageService storageService;

    @Value("${default.wine.url}")
    private String defaultImageUrl;

    @Override
    public PageResponse<AdminWinePreviewResponse> searchWinesAdmin(String searchName, String wineSort, String wineVariety, String wineCountry, Pageable pageable) {

        if(searchName != null) {
            searchName = searchName.replace(" ", "").toLowerCase();
        }
        List<AdminWinePreviewResponse> winePreviewList =
                wineRepository.searchByNameSortVarietyAndArea(searchName, wineSort, wineVariety, wineCountry, pageable)
                        .stream()
                        .map(AdminWinePreviewResponse::of)
                        .toList();
        long total = wineRepository.countSearchWineSortVarietyAndArea(searchName, wineSort, wineVariety, wineCountry);
        return PageResponse.of(new PageImpl<>(winePreviewList, pageable, total));
    }

    @Override
    public AdminWineResponse getWine(Long wineId) {
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));
        return AdminWineResponse.of(wine);
    }

    @Override
    public void saveWine(WineRegisterRequest wineRegisterRequest, MultipartFile imageFile) {
        Wine wine = Wine.create(wineRegisterRequest);
        String cleanedName = wineRegisterRequest.getName().replaceAll("[ ,.'\\\\]", "").toLowerCase();
        String cleanedNameEng = wineRegisterRequest.getNameEng().replaceAll("[ ,.'\\\\]", "").toLowerCase();
        wine.updateSearchName(cleanedName.concat(cleanedNameEng));

        String imageUrl;
        if (imageFile != null) imageUrl = storageService.uploadFile(imageFile, StoragePathName.WINE);
        else imageUrl = defaultImageUrl;

        wine.updateImageUrl(imageUrl);
        wineRepository.save(wine);

        List<WineVintage> vintages = IntStream.rangeClosed(1970, 2024)
                .mapToObj(y -> WineVintage.create(y, wine))
                .toList();
        vintages.add(WineVintage.create(0, wine));

        wineVintageRepository.saveAll(vintages);
    }

    @Override
    public void updateWine(Long wineId, WineUpdateRequest request, MultipartFile imageFile) {
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        if (request != null) {
            wine.updateWine(request.getName(), request.getNameEng(), request.getPrice(), request.getSort(),
                    request.getCountry(), request.getRegion(), request.getVariety(), request.getVivinoRating());
        }

        if(imageFile != null){
            String originalImageUrl = wine.getImageUrl();
            String newImageUrl = storageService.uploadFile(imageFile, StoragePathName.WINE);

            if (!originalImageUrl.equals(defaultImageUrl)) {
                try {
                    storageService.deleteFile(originalImageUrl);
                } catch (Exception e) {
                    storageService.deleteFile(newImageUrl);
                    throw new GeneralException(ErrorStatus.FILE_DELETE_FAILED);
                }
            }
            wine.updateImageUrl(newImageUrl);
        }
    }
}
