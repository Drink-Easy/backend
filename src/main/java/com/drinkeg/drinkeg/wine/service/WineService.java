package com.drinkeg.drinkeg.wine.service;

import com.drinkeg.drinkeg.wine.domain.Wine;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseWithThreeReviewsDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.io.IOException;
import java.util.List;

public interface WineService {

    public List<SearchWineResponseDTO> searchWinesByName(String searchName, PrincipalDetail principalDetail);

    public Wine findWineById(Long wineId);

    public WineResponseWithThreeReviewsDTO getWineResponseByWineId(Long wineId);

    public List<WineReviewResponseDTO> getWineReviewsByWineId(Long wineId);

    public HomeResponseDTO getHomeResponse(PrincipalDetail principalDetail);

    public void uploadWineImage() throws IOException;

}
