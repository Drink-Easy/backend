package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineSaleDTO.request.WineSaleRequestDTO;
import com.drinkeg.drinkeg.dto.WineSaleDTO.response.WineSaleResponseDTO;
import com.drinkeg.drinkeg.service.wineSaleService.WineSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-sale")
public class WineSaleController {
    private final WineSaleService wineSaleService;

    //와인 판매 전체 조회
    @GetMapping("")
    public ApiResponse<List<WineSaleResponseDTO>> getAllWineSales() {
        List<WineSaleResponseDTO> wineSaleResponseDTOS = wineSaleService.getAllWineSales();
        return ApiResponse.onSuccess(wineSaleResponseDTOS);
    }
    //와인 판매 아이디로 조회
    @GetMapping("/{wineSaleId}")
    public ApiResponse<WineSaleResponseDTO> getWineSale(@PathVariable Long wineSaleId) {
        WineSaleResponseDTO wineSaleResponseDTO = wineSaleService.getWineSaleById(wineSaleId);
        return ApiResponse.onSuccess(wineSaleResponseDTO);
    }

    //와인 판매 와인 스토어로 조회
    @GetMapping("/wine-store/{wineStoreId}")
    public ApiResponse<List<WineSaleResponseDTO>> getAllWineSalesByWineStore(@PathVariable Long wineStoreId) {
        List<WineSaleResponseDTO> wineSaleResponseDTOS = wineSaleService.getAllWineSalesByWineStore(wineStoreId);
        return ApiResponse.onSuccess(wineSaleResponseDTOS);
    }

    // 와인 판매 와인으로 조회
    @GetMapping("/wine/{wineId}")
    public ApiResponse<List<WineSaleResponseDTO>> getALlWineSalesByWine(@PathVariable Long wineId) {
        List<WineSaleResponseDTO> wineSaleResponseDTOS = wineSaleService.getAllWineSalesByWine(wineId);
        return ApiResponse.onSuccess(wineSaleResponseDTOS);
    }

    // 와인 판매 생성
    @PostMapping("")
    public ApiResponse<WineSaleResponseDTO> creatWineSale(@RequestBody WineSaleRequestDTO wineSaleRequestDTO) {
        WineSaleResponseDTO wineSaleResponseDTO = wineSaleService.createWineSale(wineSaleRequestDTO);
        return ApiResponse.onSuccess(wineSaleResponseDTO);
    }

    //와인 판매 수정
    @PutMapping("/{wineSaleId}")
    public ApiResponse<WineSaleResponseDTO> updateWineSale(@PathVariable Long wineSaleId, @RequestBody WineSaleRequestDTO wineSaleRequestDTO) {
        WineSaleResponseDTO wineSaleResponseDTO = wineSaleService.updateWineSale(wineSaleId, wineSaleRequestDTO);
        return ApiResponse.onSuccess(wineSaleResponseDTO);
    }

    //와인 판매 삭제
    @DeleteMapping("/{wineSaleId}")
    public ApiResponse<String> deleteWineSale(@PathVariable Long wineSaleId) {
        wineSaleService.deleteWineSaleById(wineSaleId);
        return ApiResponse.onSuccess("와인 판매 삭제 성공");
    }
}
