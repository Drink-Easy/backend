package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineNewsBookMarkDTO.request.WineNewsBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineNewsBookMarkDTO.response.WineNewsBookMarkResponseDTO;
import com.drinkeg.drinkeg.service.wineNewsBookMarkService.WineNewsBookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-news/bookmark")
public class WineNewsBookMarkController {
    private final WineNewsBookMarkService WineNewsBookMarkService;

    @PostMapping("/user/{userId}")
    public ApiResponse<WineNewsBookMarkResponseDTO> createWineNewsBookMark(@PathVariable Long userId, @RequestBody WineNewsBookMarkRequestDTO WineNewsBookMarkRequestDTO) {
        WineNewsBookMarkResponseDTO wineNewsBookMarkResponseDTO = WineNewsBookMarkService.saveWineNewsBookMark(userId, WineNewsBookMarkRequestDTO);
        return ApiResponse.onSuccess(wineNewsBookMarkResponseDTO);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<WineNewsBookMarkResponseDTO>> getWineNewsBookMarkByUserId(@PathVariable Long userId) {
        List<WineNewsBookMarkResponseDTO> wineNewsBookMarkResponseDTOS = WineNewsBookMarkService.getWineNewsBookMarksByUserId(userId);
        return ApiResponse.onSuccess(wineNewsBookMarkResponseDTOS);
    }

    @DeleteMapping("/{WineNewsBookMarkId}")
    public ApiResponse<String> deleteWineNewsBookMark(@PathVariable Long WineNewsBookMarkId) {
        WineNewsBookMarkService.deleteWineNewsBookMarkById(WineNewsBookMarkId);
        return ApiResponse.onSuccess("와인 클래스 북마크 삭제 완료.");
    }
}
