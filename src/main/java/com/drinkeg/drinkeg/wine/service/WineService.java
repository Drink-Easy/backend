package com.drinkeg.drinkeg.wine.service;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.wine.domain.Wine;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponse;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.io.IOException;
import java.util.List;

public interface WineService {

    public List<SearchWineResponseDTO> searchWinesByName(String searchName, PrincipalDetail principalDetail);

    public Wine findWineById(Long wineId);

    public WineResponse getWineResponseByWineId(Long wineId);

    public List<WineReviewResponseDTO> getWineReviewsByWineId(Long wineId);

    public HomeResponseDTO getHomeResponse(Member member);

    public void uploadWineImage() throws IOException;

}
