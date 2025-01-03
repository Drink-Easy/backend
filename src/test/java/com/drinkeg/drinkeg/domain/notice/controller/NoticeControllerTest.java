package com.drinkeg.drinkeg.domain.notice.controller;

import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import com.drinkeg.drinkeg.domain.notice.service.response.NoticeResponse;
import com.drinkeg.drinkeg.domain.notice.service.NoticeService;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoticeController.class)
class NoticeControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private NoticeService noticeService;

    @DisplayName("전체 공지사항을 조회한다.")
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void findAllNotices() throws Exception {
        // given
        when(noticeService.findAllNotice())
            .thenReturn(List.of(
                    createNoticeResponse(1L, "공지사항1", "https://notion/drinkeg/notice1", NoticeTag.NOTICE, LocalDate.now()),
                    createNoticeResponse(2L, "이벤트1", "https://notion/drinkeg/event1", NoticeTag.EVENT, LocalDate.now())
                ));
        // when // then
        mockMvc.perform(get("/notice"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.length()").value(2))
                .andExpect(jsonPath("$.result[0].id").value(1))
                .andExpect(jsonPath("$.result[1].id").value(2));
    }

    @DisplayName("전체 공지사항을 조회하는데 공지사항이 존재하지 않는다면 빈리스트를 가지는 응답을 반환한다.")
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void findAllNoticesWithEmptyNotice() throws Exception {
        // given
        when(noticeService.findAllNotice())
                .thenReturn(List.of());
        // when // then
        mockMvc.perform(get("/notice"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").isArray());
    }

    @DisplayName("단일 공지사항을 조회한다.")
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void findNoticeByID() throws Exception {
        // given
        Long id = 1L;
        when(noticeService.findNoticeById(id)).thenReturn(createNoticeResponse(id, "공지사항1", "https://notion/drinkeg/notice1", NoticeTag.NOTICE, LocalDate.now()));
        // when // then
        mockMvc.perform(get("/notice/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.title").value("공지사항1"));

    }

    @DisplayName("단일 공지사항을 잘못된 아이디로 조회하면 오류를 발생시킨다.")
    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void findNoticeByIdWithWrongId() throws Exception {
        // given
        Long wrongId = 1234567890L;
        when(noticeService.findNoticeById(wrongId))
                .thenThrow(new GeneralException(ErrorStatus.NOTICE_NOT_FOUND));
        // when // then
        mockMvc.perform(get("/notice/" + wrongId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NOTICE4001"));

    }

    private NoticeResponse createNoticeResponse(Long id, String title, String contentUrl, NoticeTag tag, LocalDate createdAt) {
        return NoticeResponse.builder()
                .id(id)
                .title(title)
                .tag(tag)
                .contentUrl(contentUrl)
                .createdAt(createdAt)
                .build();
    }
}