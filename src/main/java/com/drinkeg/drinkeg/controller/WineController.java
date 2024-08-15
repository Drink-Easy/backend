package com.drinkeg.drinkeg.controller;


import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.converter.WineConverter;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteWineRequestDTO;
import com.drinkeg.drinkeg.dto.WineDTO.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.WineReviewResponseDTO;
import com.drinkeg.drinkeg.service.wineService.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine")
public class WineController {

    private final WineService wineService;


    // 검색
    @GetMapping("")
    public ApiResponse<List<SearchWineResponseDTO>> searchWine(@RequestBody NoteWineRequestDTO noteWineRequestDTO) {
        List<SearchWineResponseDTO> searchWineResponseDTOS = wineService.searchWinesByName(noteWineRequestDTO);
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
    public ApiResponse<?> showWineReview(@PathVariable("wineId") Long wineId) {

        Wine foundWine = wineService.findWineById(wineId);
        List<TastingNote> tastingNoteList = foundWine.getTastingNoteList();
        if (tastingNoteList.isEmpty()){
            return ApiResponse.onSuccess("리뷰가 없습니다.");
        }
        List<WineReviewResponseDTO> wineReviewResponseDTOList = tastingNoteList.stream()
                .map(WineConverter::toWineReviewResPonseDTO).toList();

        return ApiResponse.onSuccess(wineReviewResponseDTOList);
    }
}
