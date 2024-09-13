package com.drinkeg.drinkeg.service.wineClassBookMarkService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.request.WineClassBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.response.WineClassBookMarkResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

import java.util.List;


public interface WineClassBookMarkService {
    public WineClassBookMarkResponseDTO saveWineClassBookMark(PrincipalDetail principalDetail, WineClassBookMarkRequestDTO wineClassBookMarkRequestDTO);
    public List<WineClassBookMarkResponseDTO> getWineClassBookMarksByUserId(PrincipalDetail principalDetail);
    public void deleteWineClassBookMarkById(PrincipalDetail principalDetail, Long wineClassBookMarkId);
}
