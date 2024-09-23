package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.apipayLoad.handler.TempHandler;
import com.drinkeg.drinkeg.dto.AppleLoginDTO.AppleLoginRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.JoinDTO;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.LoginResponseDTO;
import com.drinkeg.drinkeg.jwt.JWTUtil;
import com.drinkeg.drinkeg.service.loginService.AppleLoginService;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.MemberRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.MemberResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.loginService.JoinService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JoinService joinService;

    @PostMapping("/join")
    public ApiResponse<?> joinProcess(@RequestBody JoinDTO joinDTO) {

        joinService.join(joinDTO);
        return ApiResponse.onSuccess("회원가입 성공");
    }


    @PatchMapping("/member")
    public ApiResponse<MemberResponseDTO> addMemberDetail(@RequestBody MemberRequestDTO memberRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {

        MemberResponseDTO memberResponseDTO = joinService.addMemberDetail(memberRequestDTO, principalDetail.getUsername());
        return ApiResponse.onSuccess(memberResponseDTO);
    }



}
