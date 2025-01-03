package com.drinkeg.drinkeg.domain.notice.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import com.drinkeg.drinkeg.domain.notice.service.response.NoticeResponse;
import com.drinkeg.drinkeg.domain.notice.repository.NoticeRepository;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class NoticeServiceTest extends IntegrationTestSupport {
    @Autowired private NoticeService noticeService;
    @Autowired private NoticeRepository noticeRepository;

    @DisplayName("공지사항을 조회하면 모든 공지사항을 반환한다.")
    @Test
    void findAllNotices() {
        // given
        Notice notice1 = createNotice("공지사항1", "https://notion/drinkeg/notice1", NoticeTag.NOTICE);
        Notice notice2 = createNotice("공지사항2", "https://notion/drinkeg/notice2", NoticeTag.NOTICE);
        Notice notice3 = createNotice("공지사항3", "https://notion/drinkeg/notice3", NoticeTag.NOTICE);
        Notice event1 = createNotice("이벤트1", "https://notion/drinkeg/event1", NoticeTag.EVENT);
        Notice evnet2 = createNotice("이벤트2", "https://notion/drinkeg/event2", NoticeTag.EVENT);
        noticeRepository.saveAll(List.of(notice1, notice2, notice3, event1, evnet2));
        // when
        List<NoticeResponse> notices = noticeService.findAllNotice();
        // then
        assertThat(notices).hasSize(5)
                .extracting( "title", "contentUrl", "tag")
                .containsExactlyInAnyOrder(
                        tuple("공지사항1", "https://notion/drinkeg/notice1", NoticeTag.NOTICE),
                        tuple("공지사항2", "https://notion/drinkeg/notice2", NoticeTag.NOTICE),
                        tuple("공지사항3", "https://notion/drinkeg/notice3", NoticeTag.NOTICE),
                        tuple("이벤트1", "https://notion/drinkeg/event1", NoticeTag.EVENT),
                        tuple("이벤트2", "https://notion/drinkeg/event2", NoticeTag.EVENT)
                );
    }

    @DisplayName("공지사항을 id로 조회한다.")
    @Test
    void findNoticeById() {
        // given
        Notice notice1 = createNotice("공지사항1", "https://notion/drinkeg/notice1", NoticeTag.NOTICE);
        noticeRepository.save(notice1);
        // when
        NoticeResponse notice = noticeService.findNoticeById(notice1.getId());
        // then
        assertThat(notice).extracting("id", "title", "contentUrl", "tag")
                .containsExactlyInAnyOrder(notice.getId(), "공지사항1", "https://notion/drinkeg/notice1", NoticeTag.NOTICE);
    }

    @DisplayName("공지사항을 잘못된 Id로 조회하면 에러가 발생한다.")
    @Test
    void findNoticeByIdWithWrongId() {
        // given
        // when // then
        assertThatThrownBy(() -> noticeService.findNoticeById(0L))
                .isInstanceOf(GeneralException.class)
                .hasMessage("존재하지 않는 공지사항입니다.");
    }

    private Notice createNotice(String title, String contentUrl, NoticeTag tag) {
        return Notice.builder()
                .title(title)
                .contentUrl(contentUrl)
                .tag(tag)
                .build();
    }
}