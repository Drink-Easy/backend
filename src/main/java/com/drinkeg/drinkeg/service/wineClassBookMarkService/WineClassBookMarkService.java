package com.drinkeg.drinkeg.service.wineClassBookMarkService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.request.WineClassBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.response.WineClassBookMarkResponseDTO;

import java.util.List;


public interface WineClassBookMarkService {
    public WineClassBookMarkResponseDTO saveWineClassBookMark(Member member, WineClass wineClass);
    public List<WineClassBookMarkResponseDTO> getWineClassBookMarksByUserId(Member member);
    public void deleteWineClassBookMarkById(Member member, Long wineClassBookMarkId);
}
