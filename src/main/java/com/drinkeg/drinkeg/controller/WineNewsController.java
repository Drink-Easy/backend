package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineNewsDTO.request.WineNewsRequestDTO;
import com.drinkeg.drinkeg.dto.WineNewsDTO.response.WineNewsResponseDTO;
import com.drinkeg.drinkeg.service.wineNewsService.WineNewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-news")
public class WineNewsController {

    private final WineNewsService wineNewsService;

    @GetMapping("")
    public ApiResponse<List<WineNewsResponseDTO>> getAllWineNewses() {
        List<WineNewsResponseDTO> wineNewsResponseDTOS = wineNewsService.getAllWineNewses();
        return ApiResponse.onSuccess(wineNewsResponseDTOS);
    }

    @GetMapping("/{wineNewsId}")
    public ApiResponse<WineNewsResponseDTO> getWineNewsById(@PathVariable Long wineNewsId) {
        WineNewsResponseDTO wineNewsResponseDTO = wineNewsService.getWineNewsById(wineNewsId);
        return ApiResponse.onSuccess(wineNewsResponseDTO);
    }

    @PostMapping("")
    public ApiResponse<String> createWineNews(@RequestBody WineNewsRequestDTO wineNewsRequestDTO) {
        wineNewsService.saveWineNews(wineNewsRequestDTO);
        return ApiResponse.onSuccess("와인클래스 생성 완료");
    }

    @PutMapping("/{wineNewsId}")
    public ApiResponse<WineNewsResponseDTO> updateWineNews(@PathVariable Long wineNewsId, @RequestBody WineNewsRequestDTO wineNewsRequestDTO) {
        WineNewsResponseDTO wineNewsResponseDTO = wineNewsService.updateWineNews(wineNewsId, wineNewsRequestDTO);
        return ApiResponse.onSuccess(wineNewsResponseDTO);
    }

    @DeleteMapping("/{wineNewsId}")
    public ApiResponse<String> deleteWineNews(@PathVariable Long wineNewsId) {
        wineNewsService.deleteWineNews(wineNewsId);
        return ApiResponse.onSuccess("와인클래스 삭제 완료");
    }
}
