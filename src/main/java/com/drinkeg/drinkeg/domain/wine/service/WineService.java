package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.dto.response.HomeWineDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponseDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;

import java.io.IOException;
import java.util.List;

public interface WineService {

    public List<WinePreviewResponseDTO> searchWinesByName(String searchName, PrincipalDetail principalDetail);

    public Wine findWineById(Long wineId);

    public WineResponseWithThreeReviewsDTO getWineResponseByWineId(Long wineId, PrincipalDetail principalDetail);

    public List<WineReviewResponseDTO> getWineReviewsAndIsLikedByWineId(Long wineId, boolean orderByLatest);

    public List<HomeWineDTO> getRecommendWineList(PrincipalDetail principalDetail);

    public List<HomeWineDTO> getMostLikedWineList();

    public void uploadWineImage() throws IOException;

}
