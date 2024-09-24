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
import com.drinkeg.drinkeg.service.loginService.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authorization", description = "스프링 시큐리티 관련 API")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JoinService joinService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "username과 password를 입력받아 회원가입을 진행합니다.")
    public ApiResponse<?> joinProcess(@RequestBody JoinDTO joinDTO) {

        joinService.join(joinDTO);
        return ApiResponse.onSuccess("회원가입 성공");
    }


    @PatchMapping("/member")
    @Operation(summary = "사용자 초기 정보 추가", description = "첫 로그인 여부에 따라 isFirst 속성이 true인 경우 사용자 초기 정보를 추가합니다.")
    public ApiResponse<MemberResponseDTO> addMemberDetail(@RequestBody MemberRequestDTO memberRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {

        MemberResponseDTO memberResponseDTO = joinService.addMemberDetail(memberRequestDTO, principalDetail.getUsername());
        return ApiResponse.onSuccess(memberResponseDTO);
    }



}
