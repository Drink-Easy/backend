package com.drinkeg.drinkeg.domain.notice.controller;

import com.drinkeg.drinkeg.domain.notice.dto.NoticeRequest;
import com.drinkeg.drinkeg.domain.notice.service.AdminNoticeService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminNoticeController {
    private final AdminNoticeService adminNoticeService;

    @PostMapping("/admin/notice")
    public ApiResponse<Long> save(@RequestBody NoticeRequest request) {
        Long savedId = adminNoticeService.save(request);
        return ApiResponse.onSuccess(savedId);
    }

    @PutMapping("/admin/notice/{noticeId}")
    public ApiResponse<String> update(@PathVariable Long noticeId, @RequestBody NoticeRequest request) {
        adminNoticeService.update(noticeId, request);
        return ApiResponse.onSuccess("공지사항 수정 완료");
    }

    @DeleteMapping("/admin/notice/{noticeId}")
    public ApiResponse<String> delete(@PathVariable Long noticeId) {
        adminNoticeService.delete(noticeId);
        return ApiResponse.onSuccess("공지사항 삭제 완료");
    }
}
