package com.drinkeg.drinkeg.domain.member.controller;


import com.drinkeg.drinkeg.domain.member.service.JoinService;
import com.drinkeg.drinkeg.domain.member.service.MemberService;
import com.drinkeg.drinkeg.domain.tastingNote.service.TastingNoteService;

import com.drinkeg.drinkeg.global.security.jwt.JWTUtil;
import com.drinkeg.drinkeg.global.security.jwt.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@ActiveProfiles("test")
public class MemberControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected JoinService joinService;

    @MockBean
    protected TokenService tokenService;

    @MockBean
    private JWTUtil jwtUtil;
}
