package com.drinkeg.drinkeg.controller;


import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.converter.WineConverter;
import com.drinkeg.drinkeg.converter.WineNoteConverter;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineReviewResponseDTO;
import com.drinkeg.drinkeg.dto.WineNoteDTO.WineNoteResponseDTO;
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
    @Operation(summary = "와인 검색", description = "와인 이름으로 와인 검색하여 searchWineResponseDTOS로 반환")
    public ApiResponse<List<SearchWineResponseDTO>> searchWine(@AuthenticationPrincipal PrincipalDetail principalDetail,
                                                               @RequestParam String searchName) {

        List<SearchWineResponseDTO> searchWineResponseDTOS = wineService.searchWinesByName(searchName, principalDetail);
        return ApiResponse.onSuccess(searchWineResponseDTOS);
    }

    // 선택한 와인 정보 출력
    @GetMapping("/{wineId}")
    @Operation(summary = "선택 와인 정보 열람", description = "선택한 와인의 정보를 wineResponseDTO에 담아 반환")
    public ApiResponse<WineResponseDTO> showWine(@PathVariable("wineId") Long wineId) {

        Wine foundWine = wineService.findWineById(wineId);
        WineResponseDTO wineResponseDTO = WineConverter.toWineResponseDTO(foundWine);

        return ApiResponse.onSuccess(wineResponseDTO);
    }

    // 와인 리뷰 보기
    @GetMapping("/review/{wineId}")
    @Operation(summary = "선택 와인 리뷰 열람", description = "선택한 와인 리뷰를 List로 반환")
    public ApiResponse<List<WineReviewResponseDTO>> showWineReview(@PathVariable("wineId") Long wineId) {

        Wine foundWine = wineService.findWineById(wineId);

        List<WineReviewResponseDTO> wineReviewResponseDTOList = foundWine.getTastingNoteList()
                .stream()
                .map(WineConverter::toWineReviewResPonseDTO).toList();

        return ApiResponse.onSuccess(wineReviewResponseDTOList);
    }

    // 와인노트 (테이스팅노트 평균) 보기
    @GetMapping("/note/{wineId}")
    @Operation(summary = "와인노트 열람", description = "선택한 와인의 wineId로 와인노트 열람(백엔드 확인용, 프론트에서는 wine 검색 시 반영됨")
    public ApiResponse<WineNoteResponseDTO> showWineNote(@PathVariable("wineId") Long wineId) {

        Wine foundWine = wineService.findWineById(wineId);
        WineNote wineNote = foundWine.getWineNote();

        WineNoteResponseDTO wineNoteResponseDTO = WineNoteConverter.toWineNoteResponseDTO(wineNote);

        return ApiResponse.onSuccess(wineNoteResponseDTO);
    }

    // 와인 이미지 업로드
    @PostMapping("/upload")
    @Operation(summary = "와인 이미지 업로드", description = "백엔드에세 와인 이미지 업로드 하기 위한 API")
    public ApiResponse<?> uploadWineImage() {
        try {
            wineService.uploadWineImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ApiResponse.onSuccess("업로드 성공");
    }
}
