package com.drinkeg.drinkeg.domain.notice.controller;

import com.drinkeg.drinkeg.domain.notice.dto.NoticeRequest;
import com.drinkeg.drinkeg.domain.notice.service.AdminNoticeService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminNoticeController {
    private final AdminNoticeService adminNoticeService;

    @Operation(
            summary = "공지사항 생성",
            description = "공지사항을 생성한다."
    )
    @PostMapping("/admin/notice")
    public ApiResponse<Long> save(@Valid @RequestBody NoticeRequest request) {
        Long savedId = adminNoticeService.save(request);
        return ApiResponse.onSuccess(savedId);
    }

    @Operation(
            summary = "공지사항 수정",
            description = "공지사항 Id로 공지사항을 수정한다."
    )
    @PutMapping("/admin/notice/{noticeId}")
    public ApiResponse<String> update(@PathVariable Long noticeId, @Valid @RequestBody NoticeRequest request) {
        adminNoticeService.update(noticeId, request);
        return ApiResponse.onSuccess("공지사항 수정 완료");
    }

    @Operation(
            summary = "공지사항 삭제",
            description = "공지사항 Id로 공지사항을 삭제한다."
    )
    @DeleteMapping("/admin/notice/{noticeId}")
    public ApiResponse<String> delete(@PathVariable Long noticeId) {
        adminNoticeService.delete(noticeId);
        return ApiResponse.onSuccess("공지사항 삭제 완료");
    }
}
