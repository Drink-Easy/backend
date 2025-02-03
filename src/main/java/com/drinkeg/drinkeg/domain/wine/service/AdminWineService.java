package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.controller.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineUpdateRequest;
import org.springframework.web.multipart.MultipartFile;


public interface AdminWineService {

    void saveWine(WineRegisterRequest wineRegisterRequest, MultipartFile imageFile);

    void updateWine(Long wineId, WineUpdateRequest wineUpdateRequest, MultipartFile imageFile);

}
