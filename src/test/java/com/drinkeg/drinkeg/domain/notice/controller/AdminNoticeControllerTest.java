package com.drinkeg.drinkeg.domain.notice.controller;

import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import com.drinkeg.drinkeg.domain.notice.controller.request.NoticeRequest;
import com.drinkeg.drinkeg.domain.notice.service.AdminNoticeService;
import com.drinkeg.drinkeg.domain.notice.service.request.NoticeServiceRequest;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AdminNoticeController.class)
class AdminNoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AdminNoticeService adminNoticeService;

    @DisplayName("공지사항 등록 요청이 들어오면 등록된 공지사항의 아이디를 반환한다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void save() throws Exception {
        // given
        when(adminNoticeService.save(any(NoticeServiceRequest.class))).thenReturn(1L);
        NoticeRequest request = createNoticeRequest("공지사항", "https://notion/notice", NoticeTag.NOTICE);
        // when // then
        mockMvc.perform(post("/admin/notice")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value(1));
    }

    @DisplayName("공지사항 수정 요청이 들어오면 공지사항을 수정한다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void update() throws Exception {
        // given
        NoticeRequest request = createNoticeRequest("공지사항", "https://notion/notice", NoticeTag.NOTICE);
        // when // then
        mockMvc.perform(put("/admin/notice/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("공지사항 수정 완료"));
    }

    @DisplayName("잘못된 아이디로 공지사항 수정 요청이 들어오면 오류를 반환한다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateWithWrongId() throws Exception {
        // given
        doThrow(new GeneralException(ErrorStatus.NOTICE_NOT_FOUND))
                .when(adminNoticeService)
                .update(any(Long.class), any(NoticeServiceRequest.class));
        NoticeRequest request = createNoticeRequest("공지사항", "https://notion/notice", NoticeTag.NOTICE);
        // when // then
        mockMvc.perform(put("/admin/notice/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("NOTICE4001"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 공지사항입니다."));
    }

    @DisplayName("공지사항 삭제 요청이 들어오면 공지사항을 삭제한다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteNotice() throws Exception {
        // given
        // when // then
        mockMvc.perform(delete("/admin/notice/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("공지사항 삭제 완료"));
    }

    @DisplayName("잘못된 아이디로 공지사항 삭제 요청이 들어오면 오류를 반환한다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteWithWrongId() throws Exception {
        // given
        doThrow(new GeneralException(ErrorStatus.NOTICE_NOT_FOUND))
                .when(adminNoticeService).delete(any(Long.class));
        // when // then
        mockMvc.perform(delete("/admin/notice/1")
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("NOTICE4001"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 공지사항입니다."));
    }

    @DisplayName("제목 없이 공지사항 등록 요청을 하면 오류를 반환한다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createWithBlankTitle() throws Exception {
        // given
        NoticeRequest request = createNoticeRequest("  ", "https://notion/notice", NoticeTag.NOTICE);
        // when // then
        mockMvc.perform(post("/admin/notice")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("제목은 필수입니다."));
    }

    @DisplayName("공지사항 url 없이 공지사항 등록 요청을 하면 오류를 반환한다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createWithBlankUrl() throws Exception {
        // given
        NoticeRequest request = createNoticeRequest("공지사항", "  ", NoticeTag.NOTICE);
        // when // then
        mockMvc.perform(post("/admin/notice")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("공지사항 url은 필수입니다."));
    }

    @DisplayName("태그 없이 공지사항 등록 요청을 하면 오류를 반환한다.")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createWithoutTag() throws Exception {
        // given
        NoticeRequest request = createNoticeRequest("공지사항", "https://notion/notice", null);
        // when // then
        mockMvc.perform(post("/admin/notice")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("공지사항 태그는 필수입니다."));
    }

    private NoticeRequest createNoticeRequest(String title, String contentUrl, NoticeTag tag) {
        return NoticeRequest.builder()
                .title(title)
                .contentUrl(contentUrl)
                .tag(tag)
                .build();
    }
}