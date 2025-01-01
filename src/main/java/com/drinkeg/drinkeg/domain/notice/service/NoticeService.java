package com.drinkeg.drinkeg.domain.notice.service;

import com.drinkeg.drinkeg.domain.notice.dto.NoticeResponse;
import com.drinkeg.drinkeg.domain.notice.repository.NoticeRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;

    // 공지사항 글 불러오기
    public List<NoticeResponse> findAllNotice() {
        return noticeRepository.findAll().stream()
                .map(NoticeResponse::of)
                .collect(Collectors.toList());
    }
    // 공지사항 글 개별 불러오기
    public NoticeResponse findNoticeById(Long id) {
        return NoticeResponse.of(noticeRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOTICE_NOT_FOUND))
        );
    }
}
