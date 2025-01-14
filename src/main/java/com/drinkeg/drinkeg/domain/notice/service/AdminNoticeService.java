package com.drinkeg.drinkeg.domain.notice.service;

import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import com.drinkeg.drinkeg.domain.notice.controller.request.NoticeRequest;
import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import com.drinkeg.drinkeg.domain.notice.repository.NoticeRepository;
import com.drinkeg.drinkeg.domain.notice.service.request.NoticeServiceRequest;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminNoticeService {
    private final NoticeRepository noticeRepository;

    public Long save(NoticeServiceRequest request) {
        Notice notice = request.toEntity();
        noticeRepository.save(notice);
        return notice.getId();
    }

    public void update(Long noticeId, NoticeServiceRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOTICE_NOT_FOUND));
        notice.update(request.getTitle(), request.getContentUrl(), NoticeTag.of(request.getTag()));
    }

    public void delete(Long noticeId) {
        if (!noticeRepository.existsById(noticeId))
            throw new GeneralException(ErrorStatus.NOTICE_NOT_FOUND);
        noticeRepository.deleteById(noticeId);
    }
}
