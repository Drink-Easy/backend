package com.drinkeg.drinkeg.domain.member.login.oauth2.controller;


import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleDTO.AppleDeleteDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleDTO.AppleLoginRequestDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils.ApplePrivateKeyGenerator;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils.AppleClientSecretGenerator;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.member.login.oauth2.dto.LoginResponseDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.kakao.kakaoLoginDTO.KakaoLoginRequestDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.kakao.kakaoService.KakaoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleService.AppleService;


import java.security.PrivateKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Tag(name = "Authorization", description = "스프링 시큐리티 관련 API")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final AppleService appleService;
    private final KakaoLoginService kakaoLoginService;
    private final ApplePrivateKeyGenerator privateKeyGenerator;
    private final AppleClientSecretGenerator appleClientSecretGenerator;

    @Value("${spring.servlet.social-login.provider.apple.private-key-path}")
    private String privateKeyPath;


    @PostMapping("/login/apple")
    @Operation(summary = "애플 로그인", description = "클라이언트에게 Identity Token을 전달 받아 유저 정보를 저장합니다.")
    public ApiResponse<LoginResponseDTO> appleLogin(@RequestBody AppleLoginRequestDTO appleLoginRequestDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start apple login controller============");
        LoginResponseDTO loginResponseDTO = appleService.appleLogin(appleLoginRequestDTO, response);
        return ApiResponse.onSuccess(loginResponseDTO);
    }

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오 로그인", description = "클라이언트에게 카카오 유저 정보를 전달 받아 저장합니다.")
    public ApiResponse<LoginResponseDTO> appleLogin(@RequestBody KakaoLoginRequestDTO kakaoLoginRequestDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start kakao login controller============");

        LoginResponseDTO loginResponseDTO = kakaoLoginService.kakaoLogin(kakaoLoginRequestDTO , response);
        return ApiResponse.onSuccess(loginResponseDTO);
    }

    @DeleteMapping("member/delete/apple")
    @Operation(summary = "애플 ", description = "애플 회원 탈퇴하고 유저 정보를 삭제합니다.")
    public ApiResponse<?> deleteApple(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody AppleDeleteDTO appleDeleteDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start apple delete controller============");

        appleService.unlinkApple(principalDetail.getUsername(),appleDeleteDTO.getAuthorizationCode(),response);

        return ApiResponse.onSuccess("애플 회원 탈퇴 성공");

    }

}
