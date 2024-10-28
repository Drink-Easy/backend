package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse; 
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeResponseDTO;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineService.WineService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @GetMapping("/home")
    public ApiResponse<HomeResponseDTO> home(@AuthenticationPrincipal PrincipalDetail principalDetail) {

        // 로그인 멤버 불러오기
        Member loadMember = memberService.loadMemberByPrincipleDetail(principalDetail);

        HomeResponseDTO homeResponseDTO = wineService.getHomeResponse(loadMember);

        return ApiResponse.onSuccess(homeResponseDTO);
    }
}
