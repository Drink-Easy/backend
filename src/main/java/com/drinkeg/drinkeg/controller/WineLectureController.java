package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.WineLectureDTO.request.WineLectureRequestDTO;
import com.drinkeg.drinkeg.dto.WineLectureDTO.response.WineLectureResponseDTO;
import com.drinkeg.drinkeg.service.wineLectureService.WineLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-lecture")
public class WineLectureController {
    private final WineLectureService wineLectureService;

    @GetMapping("")
    public ApiResponse<List<WineLectureResponseDTO>> getAllWineLectures() {
        List<WineLectureResponseDTO> wineLectureResponseDTOS = wineLectureService.getAllWineLectures();
        return ApiResponse.onSuccess(wineLectureResponseDTOS);
    }

    @GetMapping("/wine-class/{wineClassId}")
    public ApiResponse<List<WineLectureResponseDTO>> getAllWineLecturesByWineClass(@PathVariable Long wineClassId) {
        List<WineLectureResponseDTO> wineLectureResponseDTOS = wineLectureService.getAllWineLecutresByWineClass(wineClassId);
        return ApiResponse.onSuccess(wineLectureResponseDTOS);
    }

    @GetMapping("/{wineLectureId}")
    public ApiResponse<WineLectureResponseDTO> getWineLectureById(@PathVariable Long wineLectureId) {
        WineLectureResponseDTO wineLectureResponseDTO = wineLectureService.getWineLecture();
        return ApiResponse.onSuccess(wineLectureResponseDTO);
    }

    @PostMapping("")
    public ApiResponse<WineLectureResponseDTO> saveWineLecture(@RequestBody WineLectureRequestDTO wineLectureRequestDTO) {
        WineLectureResponseDTO wineLectureResponseDTO = wineLectureService.saveWineLecture(wineLectureRequestDTO);
        return ApiResponse.onSuccess(wineLectureResponseDTO);
    }

    @PutMapping("/{wineLectureId}")
    public ApiResponse<WineLectureResponseDTO> updateWineLecture(@PathVariable Long wineLectureId, @RequestBody WineLectureRequestDTO wineLectureRequestDTO) {
        WineLectureResponseDTO wineLectureResponseDTO = wineLectureService.updateWineLecture(wineLectureRequestDTO, wineLectureId);
        return ApiResponse.onSuccess(wineLectureResponseDTO);
    }

    @DeleteMapping("{wineLectureId}")
    public ApiResponse<String> deleteWineLectureById(@PathVariable Long wineLectureId) {
        wineLectureService.deleteWineLecture(wineLectureId);
        return ApiResponse.onSuccess("와인 강의 삭제 성공");
    }

}
