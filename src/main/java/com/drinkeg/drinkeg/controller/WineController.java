package com.drinkeg.drinkeg.controller;


import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.converter.WineConverter;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.wineService.WineService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine")
public class WineController {

    private final WineService wineService;


    // 검색
    @GetMapping
    public ApiResponse<List<SearchWineResponseDTO>> searchWine(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                               @RequestParam String searchName) {

        List<SearchWineResponseDTO> searchWineResponseDTOS = wineService.searchWinesByName(principalDetail, searchName);
        return ApiResponse.onSuccess(searchWineResponseDTOS);
    }

    // 선택한 와인 정보 출력
    @GetMapping("/{wineId}")
    public ApiResponse<WineResponseDTO> showWine(@PathVariable("wineId") Long wineId) {

        Wine foundWine = wineService.findWineById(wineId);
        WineResponseDTO wineResponseDTO = WineConverter.toWineResponseDTO(foundWine);

        return ApiResponse.onSuccess(wineResponseDTO);
    }

    // 와인 리뷰 보기
    @GetMapping("/review/{wineId}")
    public ApiResponse<List<WineReviewResponseDTO>> showWineReview(@PathVariable("wineId") Long wineId) {

        Wine foundWine = wineService.findWineById(wineId);

        List<WineReviewResponseDTO> wineReviewResponseDTOList = foundWine.getTastingNoteList()
                .stream()
                .map(WineConverter::toWineReviewResPonseDTO).toList();

        return ApiResponse.onSuccess(wineReviewResponseDTOList);
    }

    // 와인 이미지 업로드
    @PostMapping("/upload")
    public ApiResponse<?> uploadWineImage() {
        try {
            wineService.uploadWineImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ApiResponse.onSuccess("업로드 성공");
    }
}
