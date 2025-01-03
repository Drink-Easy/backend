package com.drinkeg.drinkeg.domain.notice.controller;

import com.drinkeg.drinkeg.domain.notice.service.AdminNoticeService;
import com.drinkeg.drinkeg.domain.notice.service.NoticeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
                AdminNoticeController.class,
                NoticeController.class
})
@ActiveProfiles("test")
public abstract class NoticeControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected NoticeService noticeService;

    @MockBean
    protected AdminNoticeService adminNoticeService;
}
