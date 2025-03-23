package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.controller.request.WineRegisterRequest;
import com.drinkeg.drinkeg.domain.wine.controller.request.WineUpdateRequest;
import com.drinkeg.drinkeg.domain.wine.dto.response.AdminWinePreviewResponse;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AdminWineService {

    PageResponse<AdminWinePreviewResponse> searchWinesAdmin(String searchName, String wineSort, String wineVariety, String wineCountry, Pageable pageable);

    void saveWine(WineRegisterRequest wineRegisterRequest, MultipartFile imageFile);

    void updateWine(Long wineId, WineUpdateRequest wineUpdateRequest, MultipartFile imageFile);

}
