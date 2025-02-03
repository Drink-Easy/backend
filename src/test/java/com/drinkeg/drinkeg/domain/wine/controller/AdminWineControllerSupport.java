package com.drinkeg.drinkeg.domain.wine.controller;

import com.drinkeg.drinkeg.domain.wine.service.AdminWineService;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        AdminWineController.class
})
@ActiveProfiles("test")
public class AdminWineControllerSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AdminWineService adminWineService;
}