package com.drinkeg.drinkeg.domain.member.login.oauth2.controller;

import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleService.AppleLoginService;
import com.drinkeg.drinkeg.domain.member.login.oauth2.dto.LoginResponseDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.kakao.kakaoLoginDTO.KakaoLoginRequestDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.kakao.kakaoService.KakaoLoginService;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.AppleLoginDTO.AppleLoginRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authorization", description = "스프링 시큐리티 관련 API")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private  final AppleLoginService appleLoginService;
    private final KakaoLoginService kakaoLoginService;

    @PostMapping("/login/apple")
    @Operation(summary = "애플로그인", description = "클라이언트에게 Identity Token을 전달 받아 유저 정보를 저장합니다.")
    public ApiResponse<LoginResponseDTO> appleLogin(@RequestBody AppleLoginRequestDTO appleLoginRequestDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start apple login controller============");
        LoginResponseDTO loginResponseDTO = appleLoginService.appleLogin(appleLoginRequestDTO, response);
        return ApiResponse.onSuccess(loginResponseDTO);
    }

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오로그인", description = "클라이언트에게 카카오 유저 정보를 전달 받아 저장합니다.")
    public ApiResponse<LoginResponseDTO> appleLogin(@RequestBody KakaoLoginRequestDTO kakaoLoginRequestDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start kakao login controller============");

        LoginResponseDTO loginResponseDTO = kakaoLoginService.kakaoLogin(kakaoLoginRequestDTO , response);
        return ApiResponse.onSuccess(loginResponseDTO);
    }


}
