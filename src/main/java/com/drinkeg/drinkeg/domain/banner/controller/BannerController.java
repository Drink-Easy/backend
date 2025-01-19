package com.drinkeg.drinkeg.domain.banner.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Banner", description = "홈 화면 배너 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/banner")
public class BannerController {
}
