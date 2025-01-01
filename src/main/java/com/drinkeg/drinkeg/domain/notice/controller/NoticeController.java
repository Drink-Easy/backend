package com.drinkeg.drinkeg.domain.notice.controller;

import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import com.drinkeg.drinkeg.domain.notice.dto.NoticeResponse;
import com.drinkeg.drinkeg.domain.notice.service.NoticeService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/notice")
    public ApiResponse<List<NoticeResponse>> getAllNotices() {
        List<NoticeResponse> notices = noticeService.findAllNotice();
        return ApiResponse.onSuccess(notices);
    }

    @GetMapping("/notice/{id}")
    public ApiResponse<NoticeResponse> getNoticeById(@PathVariable Long id) {
        NoticeResponse notice = noticeService.findNoticeById(id);
        return ApiResponse.onSuccess(notice);
    }
}
