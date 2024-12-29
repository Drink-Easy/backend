package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.wine.dto.response.HomeResponseDTO;
import com.drinkeg.drinkeg.domain.member.service.MemberService;
import com.drinkeg.drinkeg.domain.wine.service.WineService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 테스트용 컨드롤러

@RestController
@RequiredArgsConstructor
public class MainController {
    private final MemberService memberService;
    private final WineService wineService;


    @GetMapping("/main")
    @Operation(summary = "main", description = "로그인 권한 체크용.")
    public ApiResponse<?> mainP() {
        return ApiResponse.onSuccess("하윙");
    }


}
