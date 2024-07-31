package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import com.drinkeg.drinkeg.service.wineClassService.WineClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-class")
public class WineClassController {

    private final WineClassService wineClassService;

    @GetMapping("")
    public ApiResponse<List<WineClassResponseDTO>> getAllWineClasses() {
        List<WineClassResponseDTO> wineClassResponseDTOS = wineClassService.getAllWineClasses();
        return ApiResponse.onSuccess(wineClassResponseDTOS);
    }

    @GetMapping("/{wineClassId}")
    public ApiResponse<WineClassResponseDTO> getWineClassById(@PathVariable Long wineClassId) {
        WineClassResponseDTO wineClassResponseDTO = wineClassService.getWineClassById(wineClassId);
        return ApiResponse.onSuccess(wineClassResponseDTO);
    }

    @PostMapping("")
    public ApiResponse<String> createWineClass(@RequestBody WineClassRequestDTO wineClassRequestDTO) {
        wineClassService.saveWineClass(wineClassRequestDTO);
        return ApiResponse.onSuccess("와인클래스 생성 완료");
    }

    @PutMapping("/{wineClassId}")
    public ApiResponse<WineClassResponseDTO> updateWineClass(@PathVariable Long wineClassId, @RequestBody WineClassRequestDTO wineClassRequestDTO) {
        WineClassResponseDTO wineClassResponseDTO = wineClassService.updateWineClass(wineClassId, wineClassRequestDTO);
        return ApiResponse.onSuccess(wineClassResponseDTO);
    }

    @DeleteMapping("/{wineClassId}")
    public ApiResponse<String> deleteWineClass(@PathVariable Long wineClassId) {
        wineClassService.deleteWineClass(wineClassId);
        return ApiResponse.onSuccess("와인클래스 삭제 완료");
    }
}
