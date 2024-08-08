package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.WineClassBookMark;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.request.WineClassBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.response.WineClassBookMarkResponseDTO;
import com.drinkeg.drinkeg.service.wineClassBookMarkService.WineClassBookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("wine-class/bookmark")
public class WineClassBookMarkController {
    private final WineClassBookMarkService wineClassBookMarkService;

    @PostMapping("/user/{userId}")
    public ApiResponse<WineClassBookMarkResponseDTO> createWineClassBookMark(@PathVariable Long userId, @RequestBody WineClassBookMarkRequestDTO wineClassBookMarkRequestDTO) {
        WineClassBookMarkResponseDTO wineClassBookMarkResponseDTO = wineClassBookMarkService.saveWineClassBookMark(userId, wineClassBookMarkRequestDTO);
        return ApiResponse.onSuccess(wineClassBookMarkResponseDTO);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<WineClassBookMarkResponseDTO>> getWineClassBookMarkByUserId(@PathVariable Long userId) {
        List<WineClassBookMarkResponseDTO> wineClassBookMarkResponseDTOS = wineClassBookMarkService.getWineClassBookMarksByUserId(userId);
        return ApiResponse.onSuccess(wineClassBookMarkResponseDTOS);
    }

    @DeleteMapping("/{wineClassBookMarkId}")
    public ApiResponse<String> deleteWineClassBookMark(@PathVariable Long wineClassBookMarkId) {
        wineClassBookMarkService.deleteWineClassBookMarkById(wineClassBookMarkId);
        return ApiResponse.onSuccess("와인 클래스 북마크 삭제 완료.");
    }
}
