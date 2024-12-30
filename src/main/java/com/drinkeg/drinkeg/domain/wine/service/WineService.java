package com.drinkeg.drinkeg.domain.wine.service;

import com.drinkeg.drinkeg.domain.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.domain.wine.dto.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeResponseDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;

import java.io.IOException;
import java.util.List;

public interface WineService {

    public List<SearchWineResponseDTO> searchWinesByName(String searchName, PrincipalDetail principalDetail);

    public Wine findWineById(Long wineId);

    public WineResponseWithThreeReviewsDTO getWineResponseByWineId(Long wineId, PrincipalDetail principalDetail);

    public WineReviewResponseDTO getWineReviewsAndIsLikedByWineId(Long wineId, PrincipalDetail principalDetail, boolean orderByLatest);

    public HomeResponseDTO getHomeResponse(PrincipalDetail principalDetail);

    public void uploadWineImage() throws IOException;

}
