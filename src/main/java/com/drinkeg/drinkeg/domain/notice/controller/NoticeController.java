package com.drinkeg.drinkeg.domain.notice.controller;

import com.drinkeg.drinkeg.domain.notice.service.response.NoticeResponse;
import com.drinkeg.drinkeg.domain.notice.service.NoticeService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(
            summary = "공지사항 전체 조회",
            description = "DB에 등록된 모든 공지사항을 조회한다. 단일조회와 결과값은 차이가 없다."
    )
    @GetMapping("/notice")
    public ApiResponse<List<NoticeResponse>> getAllNotices() {
        List<NoticeResponse> notices = noticeService.findAllNotice();
        return ApiResponse.onSuccess(notices);
    }

    @Operation(
            summary = "공지사항 단일 조회",
            description = "공지사항 Id로 공지사항을 조회한다."
    )
    @GetMapping("/notice/{id}")
    public ApiResponse<NoticeResponse> getNoticeById(@PathVariable Long id) {
        NoticeResponse notice = noticeService.findNoticeById(id);
        return ApiResponse.onSuccess(notice);
    }
}
