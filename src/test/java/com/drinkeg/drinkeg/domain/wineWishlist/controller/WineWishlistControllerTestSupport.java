package com.drinkeg.drinkeg.domain.wineWishlist.controller;

import com.drinkeg.drinkeg.domain.wineWishlist.service.WineWishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
            WineWishlistController.class
        })
@ActiveProfiles("test")
public class WineWishlistControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected WineWishlistService wineWishlistService;
}