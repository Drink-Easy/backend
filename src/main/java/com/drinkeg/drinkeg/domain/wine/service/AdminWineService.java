package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.dto.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.dto.request.WineUpdateRequest;
import com.drinkeg.drinkeg.domain.wine.dto.response.AdminWinePreviewResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.AdminWineResponse;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface AdminWineService {

    PageResponse<AdminWinePreviewResponse> searchWinesAdmin(String searchName, String wineSort, String wineVariety, String wineCountry, Pageable pageable);

    AdminWineResponse getWine(Long wineId);

    void saveWine(WineRegisterRequest wineRegisterRequest, MultipartFile imageFile);

    void updateWine(Long wineId, WineUpdateRequest wineUpdateRequest, MultipartFile imageFile);



}
