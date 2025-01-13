package com.drinkeg.drinkeg.domain.tastingNote.controller;

import com.drinkeg.drinkeg.domain.tastingNote.service.TastingNoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TastingNoteController.class)
@ActiveProfiles("test")
public class TastingNoteControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected TastingNoteService tastingNoteService;
}
