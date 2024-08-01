package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.JoinDTO;
import com.drinkeg.drinkeg.service.loginService.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ApiResponse<?> joinProcess(@RequestBody JoinDTO joinDTO) {

        joinService.joinProcess(joinDTO);

        return ApiResponse.onSuccess("회원가입 성공");
    }
}