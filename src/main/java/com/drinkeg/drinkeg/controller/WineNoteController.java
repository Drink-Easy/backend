package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.service.wineNoteService.WineNoteService;
import com.drinkeg.drinkeg.service.wineService.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wineNote")
public class WineNoteController {

    private final WineNoteService wineNoteService;
    private final WineService wineService;

    // 와인 평균 업데이트
    @PostMapping("/{wineId}")
    public ApiResponse<String> updateWineNote(@PathVariable Long wineId) {

        Wine foundWine = wineService.findWineById(wineId);
        WineNote wineNote = foundWine.getWineNote();

        wineNoteService.updateWineNote(wineNote);

        return ApiResponse.onSuccess("노트 작성 완료");
    }
}
