package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.securityDTO.jwtDTO.JoinDTO;
import com.drinkeg.drinkeg.service.loginService.MemberService;
import com.drinkeg.drinkeg.service.loginService.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @PostMapping("/join")
    public ApiResponse<?> joinProcess(@RequestBody JoinDTO joinDTO) {

        memberService.join(joinDTO);
        return ApiResponse.onSuccess("회원가입 성공");
    }

    @PostMapping("/reissue")
    public ApiResponse<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        tokenService.reissueRefreshToken(request, response);
        return ApiResponse.onSuccess("리프레쉬 토큰 재발급 성공");
    }
}
