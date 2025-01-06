package com.drinkeg.drinkeg.domain.notice.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import com.drinkeg.drinkeg.domain.notice.repository.NoticeRepository;
import com.drinkeg.drinkeg.domain.notice.service.request.NoticeServiceRequest;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.drinkeg.drinkeg.domain.notice.domain.NoticeTag.*;
import static org.assertj.core.api.Assertions.*;

class AdminNoticeServiceTest extends IntegrationTestSupport {
    @Autowired private AdminNoticeService adminNoticeService;
    @Autowired private NoticeRepository noticeRepository;

    @DisplayName("공지사항 등록 요청이 오면 공지사항을 등록한다.")
    @Test
    void saveNotice() {
        // given
        NoticeServiceRequest request = createNoticeServiceRequest("공지사항1", "https://notion/test/notice", NOTICE);
        // when
        Long savedId = adminNoticeService.save(request);
        // then
        assertThat(savedId).isNotNull();
    }

    @DisplayName("공지사항 수정 요청이 들어오면 공지사항을 수정한다.")
    @Test
    void updateNotice() {
        // given
        Notice notice = noticeRepository.save(createNotice("공지사항1", "https://notion/test/notice", NOTICE));
        NoticeServiceRequest request = createNoticeServiceRequest("이벤트1", "https://notion/test/notice/updated", EVENT);
        // when
        adminNoticeService.update(notice.getId(), request);
        // then
        assertThat(noticeRepository.findById(notice.getId()).orElse(null))
                .extracting("title", "contentUrl", "tag")
                .containsExactly("이벤트1", "https://notion/test/notice/updated", EVENT);
    }

    @DisplayName("공지사항 수정을 잘못된 아이디로 요청하는 경우 에러가 발생한다.")
    @Test
    void updateNoticeWithWrongId() {
        // given
        NoticeServiceRequest request = createNoticeServiceRequest("이벤트1", "https://notion/test/notice/updated", EVENT);
        // when // then
        assertThatThrownBy(() -> adminNoticeService.update(0L, request))
                .isInstanceOf(GeneralException.class)
                .hasMessage("존재하지 않는 공지사항입니다.");
    }

    @DisplayName("공지사항을 아이디로 삭제한다.")
    @Test
    void deleteNotice() {
        // given
        Notice notice1 = createNotice("공지사항1", "https://notion/drinkeg/notice1", NoticeTag.NOTICE);
        noticeRepository.save(notice1);
        // when
        adminNoticeService.delete(notice1.getId());
        // then
        assertThat(noticeRepository.existsById(notice1.getId())).isFalse();
    }

    @DisplayName("공지사항을 잘못된 아이디로 삭제하면 오류가 발생한다.")
    @Test
    void deleteNoticeWithWrongId() {
        // given
        // when // then
        assertThatThrownBy(() -> adminNoticeService.delete(-1L))
                .isInstanceOf(GeneralException.class)
                .hasMessage("존재하지 않는 공지사항입니다.");
    }

    private NoticeServiceRequest createNoticeServiceRequest(String title, String contentUrl, NoticeTag tag) {
        return NoticeServiceRequest.builder()
                .title(title)
                .contentUrl(contentUrl)
                .tag(tag)
                .build();
    }

    private Notice createNotice(String title, String contentUrl, NoticeTag tag) {
        return Notice.builder()
                .title(title)
                .contentUrl(contentUrl)
                .tag(tag)
                .build();
    }
}