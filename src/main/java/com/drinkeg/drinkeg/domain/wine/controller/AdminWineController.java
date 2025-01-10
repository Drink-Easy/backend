package com.drinkeg.drinkeg.domain.wine.controller;

import com.drinkeg.drinkeg.domain.wine.service.AdminWineService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.opencsv.exceptions.CsvException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Wine Admin", description = "와인 관리자 API")
@RestController
@RequiredArgsConstructor
public class AdminWineController {
    private final AdminWineService adminWineService;

    // 와인 이미지 업로드
    @PostMapping("/admin/wine/img/db")
    @Operation(
            summary = "와인 이미지 업로드 후 데이터베이스에 저장",
            description = "와인 이미지 파일은 있지만 데이터베이스에는 없는 와인에 대해 업데이트 하기 위한 API"
    )
    public ApiResponse<?> uploadWineImageDB() {
        try {
            adminWineService.uploadWineImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ApiResponse.onSuccess("업로드 후 DB저장 성공");
    }

    // 와인 이미지 업로드
    @PostMapping("/admin/wine/img/csv")
    @Operation(
            summary = "와인 이미지 업로드 후 csv에 url 저장",
            description = "csv에서 받아온 와인들에 대해 와인 이미지 업로드 후 csv에 url 저장"
    )
    public ApiResponse<?> uploadWineImageCSV() {
        try {
            adminWineService.uploadWineImageCSV();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        return ApiResponse.onSuccess("업로드 후 csv저장 성공");
    }
}
