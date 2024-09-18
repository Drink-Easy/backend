package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.service.wineLectureService.WineLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-lecture")
public class WineLectureController {
    private final WineLectureService wineLectureService;

}
