package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.converter.WineNoteConverter;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.dto.WineNoteResponseDTO;
import com.drinkeg.drinkeg.service.wineNoteService.WineNoteService;
import com.drinkeg.drinkeg.service.wineService.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-note")
public class WineNoteController {

    private final WineNoteService wineNoteService;
    private final WineService wineService;

    // 와인 평균 업데이트
    @PatchMapping("/{wineId}")
    public ApiResponse<String> updateWineNote(@PathVariable("wineId") Long wineId) {

        Wine foundWine = wineService.findWineById(wineId);
        System.out.println("foundWine = " + foundWine);
        WineNote wineNote = foundWine.getWineNote();

        wineNoteService.updateWineNote(wineNote);

        return ApiResponse.onSuccess("와인 평균 노트 업데이트 완료");
    }

    @GetMapping("/{wineId}")
    public ApiResponse<WineNoteResponseDTO> showWineNote(@PathVariable("wineId") Long wineId) {

        Wine foundWine = wineService.findWineById(wineId);
        WineNote wineNote = foundWine.getWineNote();

        WineNoteResponseDTO wineNoteResponseDTO = WineNoteConverter.toWineNoteResponseDTO(wineNote);

        return ApiResponse.onSuccess(wineNoteResponseDTO);
    }
}
