package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.controller.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineUpdateRequest;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminWineServiceImpl implements AdminWineService {
    private final WineRepository wineRepository;
    private final StorageService storageService;

    @Value("${default.wine.url}")
    private String defaultImageUrl;

    @Override
    public void saveWine(WineRegisterRequest wineRegisterRequest, MultipartFile imageFile) {
        Wine wine = Wine.of(wineRegisterRequest);

        String imageUrl;
        if (imageFile != null) imageUrl = storageService.uploadFile(imageFile, StoragePathName.WINE);
        else imageUrl = defaultImageUrl;

        wine.updateImageUrl(imageUrl);
        wineRepository.save(wine);
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
