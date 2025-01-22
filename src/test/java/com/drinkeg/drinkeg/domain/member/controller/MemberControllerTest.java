package com.drinkeg.drinkeg.domain.member.controller;

import com.drinkeg.drinkeg.domain.member.dto.JoinRequest;
import com.drinkeg.drinkeg.global.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;


import static org.mockito.ArgumentMatchers.refEq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



public class MemberControllerTest extends MemberControllerTestSupport{

    @DisplayName("회원가입 성공")
    @Test
    @AutoConfigureMockMvc(addFilters = false)
    void joinProcess_Success() throws Exception {
        // given
        JoinRequest joinRequest = new JoinRequest("testUser", "password123@","password123@");

        doNothing().when(joinService).join(refEq(joinRequest));

        // when & then: API 호출 및 응답 검증
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest)) // 요청 데이터를 JSON으로 변환
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk()) // HTTP 상태 코드 200 검증
                .andExpect(jsonPath("$.code").value("COMMON200")) // 응답 코드 검증
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("회원가입 성공"));
    }
}
